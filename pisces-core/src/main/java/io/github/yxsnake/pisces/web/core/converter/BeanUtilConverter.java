package io.github.yxsnake.pisces.web.core.converter;

import cn.hutool.core.bean.BeanUtil;

public class BeanUtilConverter {

  public static <T> T convert(Class<T> targetClass, Object source) {
    T bean = BeanUtil.toBean(source, targetClass);
    return bean;
  }

}