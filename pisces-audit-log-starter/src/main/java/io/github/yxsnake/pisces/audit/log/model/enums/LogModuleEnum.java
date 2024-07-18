package io.github.yxsnake.pisces.audit.log.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LogModuleEnum implements IBaseEnum<String> {

  LOGIN("0000","登录"),

  EMP("0001","员工模块"),

  MEMBER("0002","会员模块"),

  ROLE("0003","角色模块"),

  RESOURCE("0004","资源模块"),

  PERMISSION("0005","权限模块"),

  ;


  private final String value;

  private final String label;

  LogModuleEnum(final String value,final String label){
    this.value = value;
    this.label = label;
  }

  public static LogModuleEnum getInstance(final String value){
    return Arrays.asList(values()).stream().findFirst().orElse(null);
  }
}
