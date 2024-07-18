package io.github.yxsnake.pisces.request.log.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 日志配置
 *
 * @author snake
 **/
@Data
@Component
@ConfigurationProperties(prefix = "request-log")
public class RequestLogProperties {

  /**
   * 是否开启, 默认true
   */
  private boolean enabled = true;
  /**
   * 扫描的包路径, 分号(;)分隔
   */
  private String scanPackages;

}
