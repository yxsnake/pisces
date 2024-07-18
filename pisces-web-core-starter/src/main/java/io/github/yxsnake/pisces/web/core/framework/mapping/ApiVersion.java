package io.github.yxsnake.pisces.web.core.framework.mapping;


import java.lang.annotation.*;

/**
 * 版本注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {

  /**
   * 版本号
   */
  double version() default 1;
}
