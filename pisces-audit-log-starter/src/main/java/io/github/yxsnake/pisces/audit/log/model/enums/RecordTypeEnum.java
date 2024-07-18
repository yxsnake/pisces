package io.github.yxsnake.pisces.audit.log.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RecordTypeEnum implements IBaseEnum<Integer> {

  OPERATION(0,"操作记录"),

  EXCEPTION(1,"异常记录")
  ;


  private final Integer value;

  private final String label;

  RecordTypeEnum(final Integer value,final String label){
    this.value = value;
    this.label = label;
  }

  public static RecordTypeEnum getInstance(final String value){
    return Arrays.asList(values()).stream().findFirst().orElse(null);
  }
}
