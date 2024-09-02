package io.github.yxsnake.pisces.redis.configuration;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yxsnake.pisces.redis.configuration.properties.CustomRedisProperties;
import io.github.yxsnake.pisces.redis.common.RedisConstants;
import io.github.yxsnake.pisces.redis.serializer.StringRedisSerializer;
import io.github.yxsnake.pisces.redis.service.RedisGeoService;
import com.google.common.base.Preconditions;
import io.github.yxsnake.pisces.redis.service.SerialNumberGeneratorService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author snake
 * @description
 * @since 2024/1/16 23:14
 */
@SuppressWarnings("unchecked")
@Slf4j
@Configuration
@RequiredArgsConstructor
@Import({CustomRedisProperties.class})
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfiguration {

  @Value("${spring.application.name:}")
  private String applicationName;

  private final RedisTemplate redisTemplate;

  private final CustomRedisProperties customRedisProperties;

  private final RedisProperties redisProperties;

  /**
   * 验证规则 <br> 默认加载 redis.biz-prefix <br> 如果该参数为空则加载 spring.application.name <br>
   * 如果二者都为空，抛出异常启动终止
   */
  @PostConstruct
  public void init() {
    validRequiredParams();
    log.info("------------ redis-starter StartUp Information -----------");
    log.info("redis-starter");
    log.info("    |-host: {}",redisProperties.getHost());
    log.info("    |-port: {}", redisProperties.getPort());
    log.info("    |-password: {}", redisProperties.getPassword());
    log.info("    |-clientType: {}", redisProperties.getClientType());
    log.info("-------------------------------------------------------------");
  }

  /**
   * 自定义序列化
   *
   * @param redisConnectionFactory
   * @return
   */
  @Bean
  @Primary
  public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);

    // 设置objectMapper:转换java对象的时候使用
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    String prefix = StrUtil.isBlank(customRedisProperties.getBizPrefix()) ? "" : customRedisProperties.getBizPrefix() + RedisConstants.BIZ_PREFIX_SPLIT;
    log.info("biz-prefix > {}", prefix);

    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(prefix);

    // 设置key/value值的序列化方式
    template.setKeySerializer(stringRedisSerializer);
    template.setValueSerializer(jackson2JsonRedisSerializer);
    template.setHashKeySerializer(stringRedisSerializer);
    template.setHashValueSerializer(jackson2JsonRedisSerializer);

    template.afterPropertiesSet();

    return redisTemplate;
  }


  /**
   * 默认加载 redis.biz-prefix, 如果该参数为空则加载 spring.application.name, 如果二者都为空，抛出异常启动终止
   */
  private void validRequiredParams() {
    if (StrUtil.isBlank(applicationName)) {
      Preconditions.checkArgument(StrUtil.isNotBlank(customRedisProperties.getBizPrefix()),
        "Missing required configuration items: redis.biz-prefix");
    } else {
      if (StrUtil.isBlank(customRedisProperties.getBizPrefix())) {
        customRedisProperties.setBizPrefix(applicationName);
        log.info(
          "The configuration item (redis.biz-prefix) is missing, using the default value > {} (spring.application.name)",
          applicationName);
      }
    }
  }
  @Bean
  public RedisGeoService redisGeoService(){
    return new RedisGeoService(redisTemplate);
  }

  @Bean
  public SerialNumberGeneratorService serialNumberGeneratorService(){
    return new SerialNumberGeneratorService(redisTemplate);
  }
}
