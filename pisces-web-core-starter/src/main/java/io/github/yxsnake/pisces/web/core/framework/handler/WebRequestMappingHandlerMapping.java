package io.github.yxsnake.pisces.web.core.framework.handler;

import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.web.core.framework.mapping.ApiVersion;
import io.github.yxsnake.pisces.web.core.configuration.properties.VersionProperties;
import io.github.yxsnake.pisces.web.core.utils.ExceptionUtils;
import io.github.yxsnake.pisces.web.core.framework.mapping.ApiVersionCondition;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 重写 RequestMappingHandlerMapping 部分方法
 */
@Slf4j
public class WebRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

   static String X_PATH_VARIABLE_MAPPING_NAME = "X-DSF-MappingName";


  private static final Map<HandlerMethod, RequestMappingInfo> HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP = Maps.newHashMap();
  private final VersionProperties versionProperties;

  public WebRequestMappingHandlerMapping(VersionProperties versionProperties) {
    this.versionProperties = versionProperties;
  }

  /**
   * 解决 PathVariable 效率低问题
   */
  @Override
  protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
    HandlerMethod handlerMethod = super.createHandlerMethod(handler, method);
    HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP.put(handlerMethod, mapping);
    super.registerHandlerMethod(handler, method, mapping);
  }

  @Override
  protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request)
    throws Exception {
    String mappingName = request.getHeader(X_PATH_VARIABLE_MAPPING_NAME);
    if (StrUtil.isBlank(mappingName)) {
      return super.lookupHandlerMethod(lookupPath, request);
    }

    log.info("Enable customization mapping > {}", mappingName);
    List<HandlerMethod> handlerMethods = super.getHandlerMethodsForMappingName(mappingName);
    if (CollectionUtils.isEmpty(handlerMethods)) {
      throw ExceptionUtils.get("Method does not exist: {}", mappingName);
    }
    if (handlerMethods.size() > 1) {
      throw ExceptionUtils.get("There are multiple methods: {}", mappingName);
    }

    HandlerMethod handlerMethod = handlerMethods.get(0);
    // 根据处理方法查找RequestMappingInfo, 用于解析路径url中的参数
    RequestMappingInfo requestMappingInfo = HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP
      .get(handlerMethod);
    if (requestMappingInfo == null) {
      throw ExceptionUtils.get("Method does not exist in map: {}", mappingName);
    }
    super.handleMatch(requestMappingInfo, lookupPath, request);
    return handlerMethod;
  }


  /**
   * 加入版本控制
   */
  @Override
  protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
    RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
    if (versionProperties.isEnabled() && info != null) {
      return createApiVersionInfo(method, handlerType, info);
    } else {
      return info;
    }
  }

  @Override
  protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
    if (versionProperties.isEnabled()) {
      ApiVersion apiVersion = (ApiVersion) AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
      return createCondition(apiVersion);
    } else {
      return null;
    }
  }

  @Override
  protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
    if (versionProperties.isEnabled()) {
      ApiVersion apiVersion = (ApiVersion) AnnotationUtils.findAnnotation(method, ApiVersion.class);
      return createCondition(apiVersion);
    } else {
      return null;
    }
  }

  private RequestMappingInfo createApiVersionInfo(Method method, Class<?> handlerType,
                                                  RequestMappingInfo info) {
    ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
    if (methodAnnotation != null) {
      RequestCondition<?> methodCondition = getCustomMethodCondition(method);
      info = createApiVersionInfo(methodAnnotation, methodCondition).combine(info);
    } else {
      ApiVersion typeAnnotation = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
      if (typeAnnotation != null) {
        RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
        info = createApiVersionInfo(typeAnnotation, typeCondition).combine(info);
      }
    }
    return info;
  }

  @SuppressWarnings("unchecked")
  private RequestMappingInfo createApiVersionInfo(ApiVersion annotation,
                                                  RequestCondition<?> customCondition) {
    double value = annotation.version();
    String[] patterns = new String[1];

    patterns[0] = ("v" + value);
//        patterns[1] = "/{version}";
//        patterns[2] = "/**";

    return new RequestMappingInfo(
      new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
        useSuffixPatternMatch(),
        useTrailingSlashMatch(), getFileExtensions()),
      new RequestMethodsRequestCondition(), new ParamsRequestCondition(),
      new HeadersRequestCondition(), new ConsumesRequestCondition(),
      new ProducesRequestCondition(), customCondition);
  }

  private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
    return apiVersion == null ? null : new ApiVersionCondition(apiVersion);
  }


}
