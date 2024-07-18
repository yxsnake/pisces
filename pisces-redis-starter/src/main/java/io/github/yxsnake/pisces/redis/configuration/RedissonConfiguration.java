package io.github.yxsnake.pisces.redis.configuration;

import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.redis.configuration.properties.CustomRedissonProperties;
import io.github.yxsnake.pisces.redis.enums.RedissonConnectTypeEnum;
import io.github.yxsnake.pisces.web.core.constant.StringPool;
import io.github.yxsnake.pisces.web.core.utils.BizAssert;
import io.github.yxsnake.pisces.redis.redisson.RedissonLockTemplate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

@Data
@Import(CustomRedissonProperties.class)
@Configuration
@ConditionalOnProperty(prefix = "redisson", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class RedissonConfiguration {

  private final CustomRedissonProperties customRedissonProperties;

  private final static String AGREEMENT_PREFIX = "redis://";

  @Bean
  public RedissonClient redissonClient() {
    BizAssert.isTrue("请配置 redisson.type ", StrUtil.isBlank(customRedissonProperties.getType()));
    BizAssert.isTrue("请配置 redisson.addresses ", StrUtil.isBlank(customRedissonProperties.getAddresses()));
    BizAssert.isTrue("请配置 redisson.password ", StrUtil.isBlank(customRedissonProperties.getPassword()));
    Config config = new Config();
    RedissonConnectTypeEnum redissonConnectTypeEnum = RedissonConnectTypeEnum.getInstance(customRedissonProperties.getType());
    if(RedissonConnectTypeEnum.SINGLE.equals(redissonConnectTypeEnum)){
      config.useSingleServer().setAddress(AGREEMENT_PREFIX +customRedissonProperties.getAddresses());
      config.useSingleServer().setPassword(customRedissonProperties.getPassword());
    }else if(RedissonConnectTypeEnum.CLUSTER.equals(redissonConnectTypeEnum)){
      List<String> nodeAddresses = new ArrayList<>();
      for (String address : customRedissonProperties.getAddresses().split(StringPool.COMMA)) {
        nodeAddresses.add(AGREEMENT_PREFIX+address);
      }
      config.useClusterServers().setNodeAddresses(nodeAddresses);
      config.useClusterServers().setPassword(customRedissonProperties.getPassword());
    }else {
      BizAssert.isTrue("redisson.type参数配置只能是single或者cluster",true);
    }
    return Redisson.create(config);
  }

  /**
   * 业务使用 直接注入该bean即可  tryLock 方法 获取锁并执行业务，会自动释放锁，无需关注释放锁
   * @return
   */
  @Bean
  public RedissonLockTemplate redissonLockTemplate() {
    RedissonLockTemplate redissonLockTemplate = new RedissonLockTemplate(redissonClient());
    return redissonLockTemplate;
  }

}
