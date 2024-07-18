package io.github.yxsnake.pisces.web.core.utils;

/**
 * @author snake
 * @description
 * @since 2024/1/15 23:08
 */
public class ClassUtils {
  /**
   * <p>
   * 请仅在确定类存在的情况下调用该方法
   * </p>
   *
   * @param name 类名称
   * @return 返回转换后的 Class
   */
  public static Class<?> toClassConfident(String name) {
    try {
      return Class.forName(name);
    } catch (ClassNotFoundException e) {
      throw ExceptionUtils.get("找不到指定的class！请仅在明确确定会有 class 的时候，调用该方法", e);
    }
  }

  /**
   * 判断class是否存在，不会初始化静态变量
   *
   * @param name 类的全路径
   * @return true 存在, false 不存在
   */
  public static boolean isPresent(String name) {
    try {
      Thread.currentThread().getContextClassLoader().loadClass(name);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }
}
