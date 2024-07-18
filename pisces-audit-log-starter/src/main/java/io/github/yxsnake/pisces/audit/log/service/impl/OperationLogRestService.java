package io.github.yxsnake.pisces.audit.log.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import io.github.yxsnake.pisces.audit.log.configuration.properties.AuditLogProperties;
import io.github.yxsnake.pisces.audit.log.service.IOperationLogService;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import io.github.yxsnake.pisces.audit.log.model.bo.OperationLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("operationLogRestService")
public class OperationLogRestService implements IOperationLogService {

  @Override
  public void addOperationLog(OperationLogDTO operationLog, AuditLogProperties auditLogProperties) {
    try {
      log.info("【rest方式】发送日志:{}",JsonUtils.objectCovertToJson(operationLog));
      // 调用接口
      if(auditLogProperties.getCallEnabled()){
        String result = HttpUtil.post(auditLogProperties.getRestApi(), BeanUtil.beanToMap(operationLog), 5000);
        log.info("【rest方式】发送日志 响应结果....................｛｝",result);
        return;
      }
      log.warn("未开启日志推送 持久化数据................................");
    } catch (Exception e) {
      log.error("发送日志发生异常.....................",e);
    }
  }
}
