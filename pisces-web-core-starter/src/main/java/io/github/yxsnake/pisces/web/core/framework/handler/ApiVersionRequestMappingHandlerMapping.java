package io.github.yxsnake.pisces.web.core.framework.handler;

import io.github.yxsnake.pisces.web.core.framework.mapping.ApiVersion;
import io.github.yxsnake.pisces.web.core.framework.mapping.ApiVersionCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
  /**
   * class condition
   * - 在class上加@ApiVersion注解&url加{version}
   * @param handlerType class type
   * @return ApiVersionCondition
   */
  @Override
  protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
    ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType,ApiVersion.class);
    return null == apiVersion ? super.getCustomTypeCondition(handlerType) : new ApiVersionCondition(apiVersion);
  }

  /**
   * method condition
   * - 在方法上加@ApiVersion注解&url加{version}
   * @param method method object
   * @return ApiVersionCondition
   */
  @Override
  protected RequestCondition<?> getCustomMethodCondition(Method method) {
    ApiVersion apiVersion = AnnotationUtils.findAnnotation(method,ApiVersion.class);
    return null == apiVersion ? super.getCustomMethodCondition(method) : new ApiVersionCondition(apiVersion);
  }

}
