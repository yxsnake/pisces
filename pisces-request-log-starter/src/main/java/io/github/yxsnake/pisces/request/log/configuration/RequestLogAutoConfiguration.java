package io.github.yxsnake.pisces.request.log.configuration;

import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.request.log.configuration.properties.RequestLogProperties;
import io.github.yxsnake.pisces.request.log.interceptor.DefaultRequestLogInterceptor;
import io.github.yxsnake.pisces.request.log.proxy.RequestLogAutoScanProxy;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 请求日志打印启动类
 *
 * @author swifthorse
 **/
@Slf4j
@Import({
  RequestLogProperties.class
})
@AllArgsConstructor
@ConditionalOnProperty(prefix = "request-log", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RequestLogAutoConfiguration {

  private final RequestLogProperties logConfig;

  private final HttpServletRequest request;

  @PostConstruct
  public void init() {
    log.info("------------ Request-log-starter StartUp Information -----------");
    if (logConfig.isEnabled()) {
      log.info("Request log is opened.");
      log.info("  |-ScanPackages: {}", StrUtil.isBlank(logConfig.getScanPackages()) ? "not set" : logConfig.getScanPackages());
    } else {
      log.info("Request log is closed.");
    }
    log.info("-------------------------------------------------------------");
  }

  @Bean
  public DefaultRequestLogInterceptor defaultRequestLogInterceptor() {
    return new DefaultRequestLogInterceptor(request);
  }

  @Bean
  public RequestLogAutoScanProxy myAutoScanProxyForClass() {
    return new RequestLogAutoScanProxy(logConfig);
  }

}
