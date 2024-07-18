package io.github.yxsnake.pisces.audit.log.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "audit-log")
public class AuditLogProperties {

  /**
   * 是否开启
   */
  private Boolean enabled;
  /**
   * 传输类型 http 或 mq
   */
  private String transferType;
  /**
   * 基于接口传输方式对应的接口地址
   */
  private String restApi;
  /**
   * 基于MQ传输方式对应的接topic名称
   */
  private String topic;
  /**
   * 实发真实发起日志传输调用
   */
  private Boolean callEnabled;
}
