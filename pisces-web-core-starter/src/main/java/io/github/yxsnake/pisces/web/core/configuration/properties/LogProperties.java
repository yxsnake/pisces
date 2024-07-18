package io.github.yxsnake.pisces.web.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Log配置
 *
 * @author snake
 **/
@Data
@Component
@ConfigurationProperties(prefix = "web-core.log")
public class LogProperties {

  /**
   * 是否开启日志级别调整接口, 默认true
   */
  private boolean levelApiEnabled = true;

  /**
   * 安全秘钥，如果为空会在每次服务启动时自动生成8位随机字符串
   */
  private String secretKey;

  /**
   * 路径, 默认/logLevel
   */
  private String path = "/logLevel";

}

