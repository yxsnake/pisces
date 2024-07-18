package io.github.yxsnake.pisces.web.core.converter;

import java.io.Serializable;

/**
 * <p>
 * 普通实体父类
 * </p>
 *
 * @author snake
 */
public interface Convert extends Serializable {

  /**
   * 获取自动转换后的JavaBean对象
   */
  default  <T> T convert(Class<T> clazz) {
    return BeanUtilConverter.convert(clazz, this);
  }
}

