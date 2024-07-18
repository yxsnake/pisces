package io.github.yxsnake.pisces.web.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "web.core")
public class WebCoreProperties {

  private String applicationName;

  private Integer serverPort;

  @NestedConfigurationProperty
  private VersionProperties version = new VersionProperties();

  @NestedConfigurationProperty
  private HealthProperties health = new HealthProperties();

}
