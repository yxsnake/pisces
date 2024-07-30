package io.github.yxsnake.pisces.mybatis.plus.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "mybatis-plus.ext")
public class MybatisPlusExtProperties {

  /**
   * 是否开启
   */
  private Boolean enabled = true;
  /**
   * 是否开启非法sql拦截校验
   */
  private Boolean illegalEnabled = false;
  /**
   * 是否开启  防止全表更新删除
   */
  private Boolean blockAttackEnabled = false;
  /**
   * 忽略租户过滤的表名称
   */
  private List<String> ignoreTables;

}
