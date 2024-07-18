package io.github.yxsnake.pisces.redis.redisson;


import java.util.concurrent.TimeUnit;

public class Example {


  private RedissonLockTemplate redissonLockTemplate;

  public Example(RedissonLockTemplate redissonLockTemplate){
    this.redissonLockTemplate = redissonLockTemplate;
  }


  public Integer test() {
    // 实现 TryLockCallback 接口 执行对应的业务
    Integer result = redissonLockTemplate.tryLock("lock-1", 1, 5, TimeUnit.SECONDS, () -> {
      // 业务代码写在这里
      System.out.println("************** doBusiness *************");
      return 0;
    });
    return result;
  }
}
