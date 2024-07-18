package io.github.yxsnake.pisces.request.log.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.request.log.provider.DefaultRequestLogFormatterProvider;
import io.github.yxsnake.pisces.request.log.bean.ExpandMessage;
import io.github.yxsnake.pisces.request.log.cons.WebLogCons;
import io.github.yxsnake.pisces.request.log.spi.RequestLogFormatterProvider;
import com.google.common.collect.Lists;
import com.nepxion.matrix.proxy.aop.AbstractInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import io.github.yxsnake.pisces.web.core.internals.ServiceBootstrap;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.InitBinder;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 默认请求日志拦截器
 *
 * @author maye
 */
@Slf4j
@Component("defaultRequestLogInterceptor")
public class DefaultRequestLogInterceptor extends AbstractInterceptor {

  private static volatile List<RequestLogFormatterProvider> requestLogFormatterProviders = null;
  private static final Object LOCK = new Object();
  private final HttpServletRequest request;
  private RequestLogFormatterProvider requestLogFormatterProvider = new DefaultRequestLogFormatterProvider();

  public DefaultRequestLogInterceptor(HttpServletRequest request) {
    this.request = request;
    getRequestLogFormatterProvider();
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Annotation[] methodAnnotations = getMethodAnnotations(invocation);
    for (Annotation methodAnnotation : methodAnnotations) {
      if(log.isDebugEnabled()){
        log.warn("methodAnnotation name :{}",methodAnnotation.annotationType().getName());
      }
      // 忽略参数绑定导致的切面拦截
      if(methodAnnotation instanceof InitBinder){
        return invocation.proceed();
      }
    }
    ExpandMessage expandMessage = getExpandMessage(invocation);
    log.info(requestLogFormatterProvider.getRequestLogMessage(request, expandMessage));

    return invocation.proceed();
  }

  private ExpandMessage getExpandMessage(MethodInvocation invocation) {
    return ExpandMessage.builder()
            .methodName(getMethodName(invocation))
            .build();
  }

  private void getRequestLogFormatterProvider() {
    synchronized (LOCK) {
      requestLogFormatterProviders = initRequestLogFormatterProviders();
    }

    if (CollUtil.isNotEmpty(requestLogFormatterProviders)) {
      // 使用优先级最高的 provider
      this.requestLogFormatterProvider = requestLogFormatterProviders.get(0);
      log.info("Loaded {} as Request log formatter.", this.requestLogFormatterProvider.getClass().getName());
    } else {
      log.warn(
              "Request log formatter fallback to {}, because it is not available in all RequestLogFormatterProviders",
              this.requestLogFormatterProvider.getClass().getName()
      );
    }
  }

  private List<RequestLogFormatterProvider> initRequestLogFormatterProviders() {
    Iterator<RequestLogFormatterProvider> requestLogFormatterProviderIterator = ServiceBootstrap.loadAll(RequestLogFormatterProvider.class);

    List<RequestLogFormatterProvider> requestLogFormatterProviders = Lists.newArrayList(requestLogFormatterProviderIterator);

    requestLogFormatterProviders.sort(Comparator.comparingInt(RequestLogFormatterProvider::getOrder));

    return requestLogFormatterProviders;
  }

  private Long getRequestTime() {
    String requestTime = request.getHeader(WebLogCons.X_REQUEST_TIME);
    try {
      return Long.parseLong(requestTime);
    } catch (Exception e) {
      return null;
    }
  }

  private String getUid() {
    String uid = request.getHeader(WebLogCons.X_UID);
    if (StrUtil.isBlank(uid)) {
      return null;
    }
    return uid;
  }

}
