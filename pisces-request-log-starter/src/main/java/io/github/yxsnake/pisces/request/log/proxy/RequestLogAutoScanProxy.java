package io.github.yxsnake.pisces.request.log.proxy;

import io.github.yxsnake.pisces.request.log.annotation.RequestLog;
import io.github.yxsnake.pisces.request.log.configuration.properties.RequestLogProperties;
import io.github.yxsnake.pisces.request.log.interceptor.DefaultRequestLogInterceptor;
import com.nepxion.matrix.proxy.aop.DefaultAutoScanProxy;
import com.nepxion.matrix.proxy.mode.ProxyMode;
import com.nepxion.matrix.proxy.mode.ScanMode;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * 请求日志AOP代理
 *
 * @author maye
 */
@Slf4j
@Component("requestLogAutoScanProxy")
public class RequestLogAutoScanProxy extends DefaultAutoScanProxy {

  private static final long serialVersionUID = 2285460109381841241L;
  private final RequestLogProperties config;

  @SuppressWarnings("rawtypes")
  private Class[] commonInterceptorClasses;

  @SuppressWarnings("rawtypes")
  private Class[] classAnnotations;

  public RequestLogAutoScanProxy(RequestLogProperties config) {
    super(config.getScanPackages(), ProxyMode.BY_CLASS_OR_METHOD_ANNOTATION, ScanMode.FOR_CLASS_OR_METHOD_ANNOTATION);
    this.config = config;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Class<? extends MethodInterceptor>[] getCommonInterceptors() {
    if (commonInterceptorClasses == null) {
      commonInterceptorClasses = new Class[]{DefaultRequestLogInterceptor.class};
    }
    return commonInterceptorClasses;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Class<? extends Annotation>[] getClassAnnotations() {
    if (classAnnotations == null) {
      classAnnotations = new Class[]{RequestLog.class};
    }
    return classAnnotations;
  }
}
