package io.github.yxsnake.pisces.web.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 版本配置
 *
 * @author snake
 **/
@Data
@Component
@ConfigurationProperties(prefix = "web-core.version")
public class VersionProperties {

  /**
   * 是否开启, 默认true
   */
  private boolean enabled = true;
}
