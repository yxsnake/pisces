package io.github.yxsnake.pisces.audit.log.service.impl;

import io.github.yxsnake.pisces.audit.log.configuration.properties.AuditLogProperties;
import io.github.yxsnake.pisces.audit.log.service.IOperationLogService;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import io.github.yxsnake.pisces.audit.log.model.bo.OperationLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("operationLogMqService")
public class OperationLogMqService implements IOperationLogService {
  @Override
  public void addOperationLog(OperationLogDTO operationLog, AuditLogProperties auditLogProperties) {
    log.info("【Mq方式】发送日志....................｛｝", JsonUtils.objectCovertToJson(operationLog));
  }
}
