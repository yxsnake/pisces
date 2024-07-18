package io.github.yxsnake.pisces.web.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.web.core.base.ErrorCode;
import io.github.yxsnake.pisces.web.core.base.IResultCode;
import io.github.yxsnake.pisces.web.core.exception.BizException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author snake
 * @description 业务断言
 * @since 2024/1/16 21:42
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BizAssert {

  public static void equals(IResultCode errorEnum, Object obj1, Object obj2, Object... params) {
    if (Objects.equals(obj1, obj2)) {
      failure(errorEnum, params);
    }
  }

  public static void isTrue(IResultCode errorEnum, boolean condition, Object... params) {
    if (condition) {
      failure(errorEnum, params);
    }
  }

  public static void isFalse(IResultCode errorEnum, boolean condition, Object... params) {
    if (!condition) {
      failure(errorEnum, params);
    }
  }

  public static void isNull(IResultCode errorEnum, Object condition, Object... params) {
    if (Objects.isNull(condition)) {
      failure(errorEnum, params);
    }
  }

  public static void nonNull(IResultCode errorEnum, Object condition, Object... params) {
    if (Objects.nonNull(condition)) {
      failure(errorEnum, params);
    }
  }

  public static void equals(String message, Object obj1, Object obj2, Object... params) {
    if (Objects.equals(obj1, obj2)) {
      failure(message, params);
    }
  }

  public static void isTrue(String message, boolean condition, Object... params) {
    if (condition) {
      failure(message, params);
    }
  }

  public static void isFalse(String message, boolean condition, Object... params) {
    if (!condition) {
      failure(message, params);
    }
  }

  public static void isNull(String message, Object condition, Object... params) {
    if (Objects.isNull(condition)) {
      failure(message, params);
    }
  }

  public static void nonNull(String message, Object condition, Object... params) {
    if (Objects.nonNull(condition)) {
      failure(message, params);
    }
  }

  /**
   * <p>
   * 失败结果
   * </p>
   *
   * @param errorEnum 异常错误码
   */
  public static void failure(IResultCode errorEnum, Object... params) {
    ErrorCode errorCode = errorEnum.convert();
    throw new BizException(errorEnum.overrideMsg(StrUtil.format(errorEnum.getMsg(), params)));
  }

  public static void failure(String message, Object... params) {
    throw new BizException(StrUtil.format(message, params));
  }

  public static void notEmpty(IResultCode errorEnum, Object[] array, Object... params) {
    if (ObjectUtil.isEmpty(array)) {
      failure(errorEnum, params);
    }
  }

  public static void noNullElements(IResultCode errorEnum, Object[] array) {
    if (array != null) {
      for (Object element : array) {
        if (element == null) {
          failure(errorEnum);
        }
      }
    }
  }

  public static void notEmpty(IResultCode errorEnum, Collection<?> collection, Object... params) {
    if (CollUtil.isEmpty(collection)) {
      failure(errorEnum, params);
    }
  }

  public static void notEmpty(IResultCode errorEnum, Map<?, ?> map, Object... params) {
    if (MapUtil.isNotEmpty(map)) {
      failure(errorEnum, params);
    }
  }

  public static void isEmpty(IResultCode errorEnum, Collection<?> collection, Object... params) {
    if (CollUtil.isNotEmpty(collection)) {
      failure(errorEnum, params);
    }
  }

  public static void isEmpty(IResultCode errorEnum, Map<?, ?> map, Object... params) {
    if (MapUtil.isNotEmpty(map)) {
      failure(errorEnum, params);
    }
  }

  public static void notEmpty(String message, Object[] array, Object... params) {
    if (ObjectUtil.isEmpty(array)) {
      failure(message, params);
    }
  }

  public static void noNullElements(String message, Object[] array) {
    if (array != null) {
      for (Object element : array) {
        if (element == null) {
          failure(message);
        }
      }
    }
  }

  public static void notEmpty(String message, Collection<?> collection, Object... params) {
    if (CollUtil.isNotEmpty(collection)) {
      failure(message, params);
    }
  }

  public static void notEmpty(String message, Map<?, ?> map, Object... params) {
    if (MapUtil.isNotEmpty(map)) {
      failure(message, params);
    }
  }

  public static void isEmpty(String message, Collection<?> collection, Object... params) {
    if (CollUtil.isEmpty(collection)) {
      failure(message, params);
    }
  }

  public static void isEmpty(String message, Map<?, ?> map, Object... params) {
    if (MapUtil.isEmpty(map)) {
      failure(message, params);
    }
  }
}
