package io.github.yxsnake.pisces.xxl.job.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author snake
 * @description
 * @since 2024/1/16 22:41
 */
@Data
@Component
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

  private Boolean enabled = true;

  private String address;

  private Integer port;

  private String token;

  private String ip;

  private String applicationName;

}
