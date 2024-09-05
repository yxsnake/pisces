package com.baomidou.mybatisplus.generator.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 扩展策略配置项
 * <p>
 * 增加controller里增删改查生成控制
 *
 * @author snake
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class KyStrategyConfig extends StrategyConfig {
  /**
   * 生成Rest Api方法
   */
  private boolean controllerRestApi = true;

}
