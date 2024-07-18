package io.github.yxsnake.pisces.redis.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class CustomRedisProperties {

  private String bizPrefix;

}
