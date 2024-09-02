package io.github.yxsnake.pisces.web.core.converter;

import io.github.yxsnake.pisces.web.core.utils.JsonUtils;

public class BeanUtilConverter {

  public static <T> T convert(Class<T> targetClass, Object source) {
    String strJson = JsonUtils.objectCovertToJson(source);
    return JsonUtils.jsonCovertToObject(strJson,targetClass);
  }

}