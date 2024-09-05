package io.github.yxsnake.pisces.web.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "web-core")
public class WebCoreProperties {

  @NestedConfigurationProperty
  private SwaggerProperties swagger;

  @NestedConfigurationProperty
  private HealthProperties health = new HealthProperties();

  @NestedConfigurationProperty
  private RequestLogProperties log = new RequestLogProperties();

  private Set<String> excludePathPatterns;


}
