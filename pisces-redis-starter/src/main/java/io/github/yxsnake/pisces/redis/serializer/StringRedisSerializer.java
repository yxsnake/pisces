package io.github.yxsnake.pisces.redis.serializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class StringRedisSerializer implements RedisSerializer<String> {

  private final Charset charset;

  private final String bizPrefix;

  public StringRedisSerializer(String bizPrefix) {
    this(StandardCharsets.UTF_8, bizPrefix);
  }

  public StringRedisSerializer(Charset charset, String bizPrefix) {

    Assert.notNull(charset, "Charset must not be null!");
    this.charset = charset;
    this.bizPrefix = bizPrefix;
  }

  @Override
  public String deserialize(@Nullable byte[] bytes) {
    return (bytes == null ? null : new String(bytes, charset));
  }

  @Override
  public byte[] serialize(@Nullable String string) {
    return (string == null ? null : (this.bizPrefix + string).getBytes(charset));
  }

}
