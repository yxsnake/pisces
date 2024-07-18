package io.github.yxsnake.pisces.web.core.factory;

import io.github.yxsnake.pisces.web.core.factory.impl.ThreadPoolServiceImpl;

/**
 * @author snake
 * @description 通用线程池
 * @since 2024/1/15 22:48
 */
public class CommonThreadPool {

  /**
   * 单例线程池
   */
  private volatile static ThreadPoolService threadPoolService;

  /**
   * 构造方法私有化
   */
  private CommonThreadPool() {

  }

  /**
   * 单例线程池获取
   *
   * @return
   */
  public static ThreadPoolService getBlockThreadPool() {
    if (threadPoolService == null) {
      synchronized (CommonThreadPool.class) {
        if (threadPoolService == null) {
          threadPoolService = new ThreadPoolServiceImpl();
        }
      }
    }
    return threadPoolService;
  }

}
