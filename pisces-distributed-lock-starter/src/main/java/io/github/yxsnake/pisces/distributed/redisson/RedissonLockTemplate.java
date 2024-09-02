package io.github.yxsnake.pisces.distributed.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class RedissonLockTemplate {

  private final RedissonClient redissonClient;

  /**
   *
   * @param lockKey 锁key
   * @param waitTime 等待时间
   * @param leaseTime 超时拖放时间
   * @param unit 时间单位
   * @param action
   * @return
   * @param <T>
   */
  public <T> T tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit, TryLockCallback<T> action) {
    RLock lock = redissonClient.getLock(lockKey);
    T result = null;
    try {
      boolean tryLock = lock.tryLock(waitTime, leaseTime, unit);
      if(tryLock) {
        result = action.doBusiness();
      }
    } catch (InterruptedException e) {
      log.error("{} 锁发生中断异常!", lockKey, e);
    } finally {
      if(lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
    return result;
  }


}