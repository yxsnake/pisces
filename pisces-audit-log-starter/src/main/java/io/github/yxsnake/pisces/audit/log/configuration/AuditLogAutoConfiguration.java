package io.github.yxsnake.pisces.audit.log.configuration;

import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.audit.log.aspect.AuditLogAspect;
import io.github.yxsnake.pisces.audit.log.configuration.properties.AuditLogProperties;
import io.github.yxsnake.pisces.audit.log.model.enums.TransferTypeEnum;
import io.github.yxsnake.pisces.audit.log.service.IOperationLogService;
import com.google.common.base.Preconditions;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Import({AuditLogProperties.class})
@Configuration
@RequiredArgsConstructor
public class AuditLogAutoConfiguration {

  @Resource
  private  AuditLogProperties auditLogProperties;

  @Resource
  private  Map<String, IOperationLogService> operationLogServiceMap;

  @PostConstruct
  public void init() {
    validRequiredParams();
    log.info("------------ audit-log-starter StartUp Information -----------");
    log.info("audit-log-starter");
    log.info("    |-audit-log.enabled: {}", auditLogProperties.getEnabled());
    log.info("    |-audit-log.transfer-type: {}", auditLogProperties.getTransferType());
    if(TransferTypeEnum.REST_TRANSFER.getValue().equals(auditLogProperties.getTransferType())){
      log.info("    |-audit-log.rest-api: {}", auditLogProperties.getRestApi());
    }else{
      log.info("    |-rocketmq.topic: {}", auditLogProperties.getTopic());
      log.info("    |-audit-log.topic: {}", auditLogProperties.getTopic());
    }
    log.info("-------------------------------------------------------------");
  }

  private void validRequiredParams(){
    Preconditions.checkArgument(Objects.nonNull(auditLogProperties.getEnabled()), "Missing required configuration items: audit-log.enabled");
    Preconditions.checkArgument(StrUtil.isNotBlank(auditLogProperties.getTransferType()), "Missing required configuration items: audit-log.transfer-type");
    if(TransferTypeEnum.REST_TRANSFER.getValue().equals(auditLogProperties.getTransferType())){
      Preconditions.checkArgument(StrUtil.isNotBlank(auditLogProperties.getRestApi()), "Missing required configuration items: audit-log.rest-api");
    }else{
      Preconditions.checkArgument(StrUtil.isNotBlank(auditLogProperties.getRestApi()), "Missing required configuration items: audit-log.topic");
    }
  }


  @Bean
  public AuditLogAspect auditLogAspect(){
    return new AuditLogAspect(operationLogServiceMap,auditLogProperties);
  }
}
