package io.github.yxsnake.pisces.web.core.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

@Getter
public enum SqlOrderTypeEnum implements IBaseEnum<Integer> {

  ASC(0,"升序"),

  DESC(1,"降序"),

  ;

  private final Integer value;

  private final String label;

  SqlOrderTypeEnum(Integer value,String label){
    this.value = value;
    this.label = label;
  }
}