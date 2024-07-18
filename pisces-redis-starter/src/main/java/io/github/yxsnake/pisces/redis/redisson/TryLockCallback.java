package io.github.yxsnake.pisces.redis.redisson;

public interface TryLockCallback<T>{

  /**
   * 执行业务
   * @return
   */
  T doBusiness();
}
