package io.github.yxsnake.pisces.audit.log.annotation;

import io.github.yxsnake.pisces.audit.log.model.enums.LogModuleEnum;
import io.github.yxsnake.pisces.audit.log.model.enums.OperationTypeEnum;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {


  /**
   * 日志描述
   * @return
   */
  String description() default "";

  /**
   * 所属微服务名称
   * @return
   */
  String microservicesName() default "";

  /**
   * 审计日志模块
   * @return
   */
  LogModuleEnum module() default LogModuleEnum.EMP;

  /**
   * 审计日志类型
   * @return
   */
  OperationTypeEnum type() default OperationTypeEnum.QUERY;
}
