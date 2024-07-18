package io.github.yxsnake.pisces.web.core.utils;

import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.web.core.exception.ServiceException;

/**
 * @author snake
 * @description
 * @since 2024/1/15 23:09
 */
public class ExceptionUtils {
  /**
   * 返回一个新的异常，统一构建，方便统一处理
   *
   * @param msg 消息
   * @param t   异常信息
   * @return 返回异常
   */
  public static ServiceException get(String msg, Throwable t, Object... params) {
    return new ServiceException(StrUtil.format(msg, params), t);
  }

  /**
   * 重载的方法
   *
   * @param msg 消息
   * @return 返回异常
   */
  public static ServiceException get(String msg, Object... params) {
    return new ServiceException(StrUtil.format(msg, params));
  }

  /**
   * 重载的方法
   *
   * @param t 异常
   * @return 返回异常
   */
  public static ServiceException get(Throwable t) {
    return new ServiceException(t);
  }
}
