package com.baomidou.mybatisplus.generator.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 扩展跟包相关的配置项
 *
 * @author shihu, zhangchi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class KyPackageConfig extends PackageConfig {

  /**
   * Dto包名
   */
  private String dto = "dto";
  /**
   * Bo包名
   */
  private String bo = "bo";
  /**
   * Form包名
   */
  private String form ="form";

  private String metaObjectHandler = "handler.mybatis";


}
