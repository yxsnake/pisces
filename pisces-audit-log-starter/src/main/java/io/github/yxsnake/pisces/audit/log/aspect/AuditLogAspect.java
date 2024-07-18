package io.github.yxsnake.pisces.audit.log.aspect;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.audit.log.annotation.AuditLog;
import io.github.yxsnake.pisces.audit.log.configuration.properties.AuditLogProperties;
import io.github.yxsnake.pisces.audit.log.model.enums.RecordTypeEnum;
import io.github.yxsnake.pisces.audit.log.service.IOperationLogService;
import io.github.yxsnake.pisces.web.core.base.LoginUser;
import io.github.yxsnake.pisces.web.core.context.UserContext;
import io.github.yxsnake.pisces.web.core.utils.IpUtil;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import io.github.yxsnake.pisces.web.core.utils.ThreadLocalUtil;
import io.github.yxsnake.pisces.audit.log.common.Cons;
import io.github.yxsnake.pisces.audit.log.model.bo.OperationLogDTO;
import io.github.yxsnake.pisces.audit.log.model.enums.TransferTypeEnum;
import com.google.common.base.Throwables;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Aspect
@Slf4j
public class AuditLogAspect {

  private  final Map<String, IOperationLogService> operationLogServiceMap;

  private  final AuditLogProperties auditLogProperties;

  public AuditLogAspect(final Map<String,IOperationLogService> operationLogServiceMap, final AuditLogProperties auditLogProperties){
      this.operationLogServiceMap = operationLogServiceMap;
      this.auditLogProperties = auditLogProperties;
  }

  /**
   * 定义注解切点，注解拦截
   */
  @Pointcut("@annotation(io.github.yxsnake.pisces.audit.log.annotation.AuditLog)")
  public void auditLog() {
  }

  /**
   * 前置通知，方法调用前被调用
   * 除非抛出一个异常，否则这个通知不能阻止连接点之前的执行流程
   */
  @Before("auditLog()")
  public void doBefore() {
    log.info("进入 @Before ...");
  }

  /**
   * 环绕增强
   * 先执行 pjp.proceed() 然后进入 @Before，然后执行主方法，回到 @Around 的 pjp.proceed() 后
   * @param pjp ProceedingJoinPoint
   * @return
   */
  @Around("auditLog()")
  public Object doAround(ProceedingJoinPoint pjp) {
    log.info("进入 @Around ......");
    // 记录一个开始时间
    long beginTime = DateUtil.date().getTime();
    ThreadLocalUtil.set(Cons.OPERATION_BEGIN_TIME_KEY,DateUtil.date().getTime());
    Object result = null;
    try {
      result = pjp.proceed();
      String operationJson = ThreadLocalUtil.get(Cons.OPERATION_LOG_OBJ_KEY);
      if(StrUtil.isNotBlank(operationJson)){
        OperationLogDTO operationLog = JsonUtils.jsonCovertToObject(operationJson, OperationLogDTO.class);
        operationLog.setResponseData(Objects.isNull(result)?null:JsonUtils.objectCovertToJson(result));
        this.buildParamAndSaveOperationLog(beginTime, operationLog);
      }

    } catch (Throwable throwable) {
      log.error("error:{}",Throwables.getStackTraceAsString(throwable));
    }
    log.info("退出 @Around ......");
    return result;
  }

  /**
   * 后置通知，如果切入点抛出异常，则不会执行
   */
  @AfterReturning(pointcut = "auditLog()", returning = "keys")
  public void doAfterReturning(JoinPoint joinPoint, Object keys) {
    OperationLogDTO operationLog = buildLog(joinPoint);
    ThreadLocalUtil.set(Cons.OPERATION_LOG_OBJ_KEY,JsonUtils.objectCovertToJson(operationLog));
  }

  /**
   * 异常返回通知，切入点抛出异常后执行
   * @param joinPoint 切入点
   * @param e 异常
   */
  @AfterThrowing(pointcut = "auditLog()", throwing = "e")
  public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
    long beginTime = ThreadLocalUtil.get(Cons.OPERATION_BEGIN_TIME_KEY);
    OperationLogDTO operationLog = buildLog(joinPoint);
    operationLog.setExceptionDesc(Throwables.getStackTraceAsString(e));
    operationLog.setRecordType(RecordTypeEnum.EXCEPTION.getValue());
    buildParamAndSaveOperationLog(beginTime, operationLog);

    try {
      log.error("进入 @AfterThrowing...错误：" + e.getMessage());
    } finally {
        ThreadLocalUtil.remove(Cons.OPERATION_LOG_OBJ_KEY);
        ThreadLocalUtil.remove(Cons.OPERATION_BEGIN_TIME_KEY);
    }
  }

  private OperationLogDTO buildLog(JoinPoint joinPoint){
    OperationLogDTO operationLog = new OperationLogDTO();
    // 获取 RequestAttributes
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    Assert.notNull(requestAttributes, "获取 RequestAttributes 为空");
    // 获取 HttpServletRequest
    HttpServletRequest request = (HttpServletRequest)requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
    Assert.notNull(request, "获取request 为空");
    // 获取注解参数信息
    // 从切入点通过反射获取切入点方法
    MethodSignature signature = (MethodSignature)joinPoint.getSignature();
    // 获取执行方法，e.g. public void com.github.snake.system.controller.EmployeeController.create()
    Method method = signature.getMethod();
    // 获取方法参数
    Object[] args = joinPoint.getArgs();

    // 获取注解
    AuditLog auditLog = method.getAnnotation(AuditLog.class);
    String description = "";
    if (null != operationLog) {
      // 解析表达式
      description = parseDescriptionExpression(auditLog.description(), args);
    }

    // 获请求参数
    AtomicReference<List<Object>> allArgs = new AtomicReference<>(asList(args));
    List<Object> requestParamsObj = allArgs.get().stream().map(arg -> {
      if (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)) {
        return arg;
      } else {
        return null;
      }
    }).filter(arg -> arg != null).collect(Collectors.toList());

    String requestParams = Objects.nonNull(requestParamsObj)? JsonUtils.objectCovertToJson(requestParamsObj):null;


    String ipAddr = IpUtil.getIpAddr(request);
    String userAgent = request.getHeader("user-agent");
    LoginUser loginUser = UserContext.get();

    operationLog.setModuleCode(auditLog.module().getValue());
    operationLog.setModuleName(auditLog.module().getLabel());
    operationLog.setMicroservicesName(auditLog.microservicesName());
    operationLog.setOperationType(auditLog.type().getValue());
    operationLog.setDescription(description);
    operationLog.setRecordType(RecordTypeEnum.OPERATION.getValue());
    operationLog.setRequestMethod(request.getMethod());
    operationLog.setRequestParams(requestParams);
    operationLog.setUserAgent(userAgent);
    operationLog.setOperationTime(DateUtil.date());
    operationLog.setOperationIp(ipAddr);
    operationLog.setOperationUserId(loginUser.getUserId());
    operationLog.setOperationUserName(loginUser.getRealName());
    return operationLog;
  }

  private void buildParamAndSaveOperationLog(long beginTime, OperationLogDTO operationLog) {
    operationLog.setCostTime(DateUtil.date().getTime()-beginTime);
    if(auditLogProperties.getEnabled()){
      String transferType = auditLogProperties.getTransferType();
      TransferTypeEnum transferTypeEnum = TransferTypeEnum.getInstance(transferType);
      IOperationLogService operationLogService = operationLogServiceMap.get(transferTypeEnum.getBeanName());
      operationLogService.addOperationLog(operationLog,auditLogProperties);
    }
  }


  /**
   * 解析 SpEL 表达式，从参数中获取数据并生成描述
   * @param descriptionExpression 描述表达式（使用 SpEL 表达式）
   * @param args 参数
   * @return
   */
  private String parseDescriptionExpression(String descriptionExpression, Object[] args) {
    SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    Expression expression = spelExpressionParser.parseExpression(descriptionExpression, new TemplateParserContext());
    return expression.getValue(new StandardEvaluationContext(args), String.class);
  }
}
