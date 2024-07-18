package io.github.yxsnake.pisces.audit.log.model.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * 审计日志写入数据库传输方式
 */
@Getter
public enum TransferTypeEnum implements IBaseEnum<String> {

  REST_TRANSFER("rest","基于http接口写入日志表","operationLogRestService"),

  MQ_TRANSFER("mq","基于rocketmq推送消息写入日志","operationLogMqService"),

  ;


  private final String value;

  private final String label;

  private final String beanName;

  TransferTypeEnum(final String value,final String label,final String beanName){
    this.value = value;
    this.label = label;
    this.beanName = beanName;
  }

  public static TransferTypeEnum getInstance(final String value){
    return Arrays.asList(values()).stream().findFirst().orElse(null);
  }
}
