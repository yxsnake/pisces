package io.github.yxsnake.pisces.redis.service;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author snake
 * @description
 * @since 2024/1/16 23:10
 */
@SuppressWarnings("unchecked")
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessSnGeneratorService {

  private final RedisTemplate redisTemplate;
  /**
   * @param businessType 业务类型
   * @param digit 业务序号位数
   * @return
   */
  public String generateSerialNo(String businessType, Integer digit) {
    if (StrUtil.isBlank(businessType)) {
      businessType = "COMMON";
    }
    String date = LocalDateTime.now(ZoneOffset.of("+8"))
      .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String key = "SN:" + businessType + ":" + date;
    Long increment = redisTemplate.opsForValue().increment(key);
    return date + String.format("%0" + digit + "d", increment);
  }

  public String generateSerialNo(Integer digit) {
    return this.generateSerialNo(null, 6);
  }

  public String generateSerialNo(String businessType) {
    return this.generateSerialNo(businessType, 6);
  }
}
