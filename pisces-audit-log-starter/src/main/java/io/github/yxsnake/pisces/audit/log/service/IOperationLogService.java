package io.github.yxsnake.pisces.audit.log.service;

import io.github.yxsnake.pisces.audit.log.configuration.properties.AuditLogProperties;
import io.github.yxsnake.pisces.audit.log.model.bo.OperationLogDTO;

public interface IOperationLogService {

  void addOperationLog(OperationLogDTO operationLog, AuditLogProperties auditLogProperties);
}
