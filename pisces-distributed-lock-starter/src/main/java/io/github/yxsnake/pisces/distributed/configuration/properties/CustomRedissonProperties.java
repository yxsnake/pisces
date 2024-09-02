package io.github.yxsnake.pisces.distributed.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redisson")
public class CustomRedissonProperties {

  /**
   * single or cluster
   */
  private String type;
  /**
   * single 127.0.0.1:6379
   * cluster 192.168.100.101:6379,192.168.100.102:6379,192.168.100.103:6379
   */
  private String addresses;

  private String password;

}