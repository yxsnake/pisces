package io.github.yxsnake.pisces.web.core.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

/**
 * @author snake
 * @description 性别枚举
 * @since 2023/8/19 22:05
 */
@Getter
public enum GenderEnum implements IBaseEnum<Integer> {

  MALE(1, "男"),
  FEMALE(2, "女"),
  UNKNOWN(0, "未知");

  private final Integer value;

  private final String label;

  GenderEnum(Integer value, String label) {
    this.value = value;
    this.label = label;
  }
}

