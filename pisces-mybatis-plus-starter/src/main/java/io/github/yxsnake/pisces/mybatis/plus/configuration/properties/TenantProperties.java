package io.github.yxsnake.pisces.mybatis.plus.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

  /**
   * 是否开启
   */
  private Boolean enabled = true;
  /**
   * 忽略租户过滤的表名称
   */
  private List<String> ignoreTables;

}
