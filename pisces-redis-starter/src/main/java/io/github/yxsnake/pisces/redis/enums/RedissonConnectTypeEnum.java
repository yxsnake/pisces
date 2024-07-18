package io.github.yxsnake.pisces.redis.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RedissonConnectTypeEnum {


  SINGLE("single","单节点"),

  CLUSTER("cluster","集群")

  ;

  private final String code;

  private final String desc;

  RedissonConnectTypeEnum(final String code,final String desc){
    this.code = code;
    this.desc = desc;
  }

  public static RedissonConnectTypeEnum getInstance(final String code){
    return Arrays.asList(values()).stream().filter(item->item.getCode().equals(code)).findFirst().orElse(null);
  }

}
