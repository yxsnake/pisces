package io.github.yxsnake.pisces.web.core.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

/**
 * @author snake
 * @description 状态枚举
 * @since 2023/8/19 22:06
 */
@Getter
public enum StatusEnum implements IBaseEnum<Integer> {

  ENABLE(1, "启用"),
  DISABLE(0, "禁用");

  private final Integer value;

  private final String label;

  StatusEnum(Integer value, String label) {
    this.value = value;
    this.label = label;
  }
}
