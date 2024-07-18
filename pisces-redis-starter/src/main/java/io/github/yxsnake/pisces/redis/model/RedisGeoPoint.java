package io.github.yxsnake.pisces.redis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author snake
 * @description 坐标信息
 * @since 2024/1/16 23:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisGeoPoint {

  /**
   * key
   */
  private String key;
  /**
   *  人唯一标识
   */
  private String userId;
  /**
   * 经度
   */
  private Double lng;
  /**
   * 纬度
   */
  private Double lat;

}
