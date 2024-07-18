package io.github.yxsnake.pisces.audit.log.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OperationTypeEnum implements IBaseEnum<Integer> {

  LOGIN(1, "登录"),

  LOGOUT(2, "注销登录"),

  INSERT(3, "新增"),

  UPDATE(4, "修改"),

  DELETE(5, "删除"),

  QUERY(6, "查询"),

  REPORT_EXPORT(7, "报表导出"),

  DATA_IMPORT(8,"数据导入")


  ;


  private final Integer value;

  private final String label;

  OperationTypeEnum(final Integer value,final String label){
    this.value = value;
    this.label = label;
  }

  public static OperationTypeEnum getInstance(final String value){
    return Arrays.asList(values()).stream().findFirst().orElse(null);
  }
}
