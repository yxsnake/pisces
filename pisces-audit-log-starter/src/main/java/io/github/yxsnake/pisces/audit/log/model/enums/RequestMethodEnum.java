package io.github.yxsnake.pisces.audit.log.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RequestMethodEnum implements IBaseEnum<String> {

  GET("GET","GET请求"),

  POST("POST","POST请求"),

  PUT("PUT","PUT请求"),

  DELETE("DELETE","DELETE请求"),

  ;


  private final String value;

  private final String label;

  RequestMethodEnum(final String value,final String label){
    this.value = value;
    this.label = label;
  }

  public static RequestMethodEnum getInstance(final String value){
    return Arrays.asList(values()).stream().findFirst().orElse(null);
  }
}
