package io.github.yxsnake.pisces.web.core.base;

import cn.hutool.core.util.ObjectUtil;

import java.util.EnumSet;
import java.util.Objects;

/**
 * @author snake
 * @description 枚举通用接口
 * @since 2023/8/19 21:58
 */
public interface IBaseEnum<T> {

  T getValue();

  String getLabel();

  /**
   * 根据值获取枚举
   *
   * @param value
   * @param clazz
   * @param <E>   枚举
   * @return
   */
  static <E extends Enum<E> & IBaseEnum> E getEnumByValue(Object value, Class<E> clazz) {
    Objects.requireNonNull(value);
    // 获取类型下的所有枚举
    EnumSet<E> allEnums = EnumSet.allOf(clazz);
    return allEnums.stream()
      .filter(e -> ObjectUtil.equal(e.getValue(), value))
      .findFirst()
      .orElse(null);
  }

  /**
   * 根据文本标签获取值
   *
   * @param value
   * @param clazz
   * @param <E>
   * @return
   */
  static <E extends Enum<E> & IBaseEnum> String getLabelByValue(Object value, Class<E> clazz) {
    Objects.requireNonNull(value);
    // 获取类型下的所有枚举
    EnumSet<E> allEnums = EnumSet.allOf(clazz);
    E matchEnum = allEnums.stream()
      .filter(e -> ObjectUtil.equal(e.getValue(), value))
      .findFirst()
      .orElse(null);

    String label = null;
    if (matchEnum != null) {
      label = matchEnum.getLabel();
    }
    return label;
  }


  /**
   * 根据文本标签获取值
   *
   * @param label
   * @param clazz
   * @param <E>
   * @return
   */
  static <E extends Enum<E> & IBaseEnum> Object getValueByLabel(String label, Class<E> clazz) {
    Objects.requireNonNull(label);
    // 获取类型下的所有枚举
    EnumSet<E> allEnums = EnumSet.allOf(clazz);
    E matchEnum = allEnums.stream()
      .filter(e -> ObjectUtil.equal(e.getLabel(), label))
      .findFirst()
      .orElse(null);

    Object value = null;
    if (matchEnum != null) {
      value = matchEnum.getValue();
    }
    return value;
  }


}

