package io.github.yxsnake.pisces.web.core.utils;

import io.github.yxsnake.pisces.web.core.constant.StringPool;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.IntStream;

/**
 * @author snake
 * @description 驼峰转换工具
 * @since 2024/1/15 23:09
 */
public class HumpUtil {

  /**
   * 驼峰转下划线
   *
   * @param value 待转换值
   * @return 结果
   */
  public static String camelToUnderscore(String value) {
    if (StringUtils.isBlank(value)) {
      return value;
    }
    String[] arr = StringUtils.splitByCharacterTypeCamelCase(value);
    if (arr.length == 0) {
      return value;
    }
    StringBuilder result = new StringBuilder();
    IntStream.range(0, arr.length).forEach(i -> {
      if (i != arr.length - 1) {
        result.append(arr[i]).append("_");
      } else {
        result.append(arr[i]);
      }
    });
    return StringUtils.lowerCase(result.toString());
  }

  /**
   * 下划线转驼峰
   *
   * @param value 待转换值
   * @return 结果
   */
  public static String underscoreToCamel(String value) {
    StringBuilder result = new StringBuilder();
    String[] arr = value.split(StringPool.UNDER_LINE);
    for (String s : arr) {
      result.append((String.valueOf(s.charAt(0))).toUpperCase()).append(s.substring(1));
    }
    return result.toString();
  }
}
