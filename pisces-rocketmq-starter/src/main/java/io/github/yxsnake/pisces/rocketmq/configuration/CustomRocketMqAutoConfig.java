package io.github.yxsnake.pisces.rocketmq.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomRocketMqAutoConfig {

  private final RocketMQProperties rocketMQProperties;

  @PostConstruct
  public void init() {
    log.info("------------ RocketMq-starter StartUp Information -----------");
    log.info("rocketmq-starter");
    log.info("  |-NameServer");
    log.info("    |-rocketmq.name-server: {}", rocketMQProperties.getNameServer());
    log.info("    |-producer");
    log.info("      |-rocketmq.producer.group: {}", rocketMQProperties.getProducer().getGroup());
    log.info("      |-rocketmq.producer.send-message-timeout: {}", rocketMQProperties.getProducer().getSendMessageTimeout());
    log.info("      |-rocketmq.producer.max-message-size: {}", rocketMQProperties.getProducer().getMaxMessageSize());
    log.info("      |-rocketmq.producer.retry-times-when-send-failed: {}", rocketMQProperties.getProducer().getRetryTimesWhenSendFailed());
    log.info("      |-rocketmq.producer.retry-times-when-send-async-failed: {}", rocketMQProperties.getProducer().getRetryTimesWhenSendAsyncFailed());
    log.info("    |-consumer");
    log.info("      |-rocketmq.consumer.group: {}", rocketMQProperties.getConsumer().getGroup());
    log.info("-------------------------------------------------------------");
  }
}