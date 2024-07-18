package io.github.yxsnake.pisces.audit.log.model.bo;

import io.github.yxsnake.pisces.audit.log.model.enums.OperationTypeEnum;
import io.github.yxsnake.pisces.audit.log.model.enums.RequestMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogDTO {
  /**
   * 模块编码
   */
  private String moduleCode;
  /**
   * 模块名称
   */
  private String moduleName;
  /**
   * 所属微服务
   */
  private String microservicesName;
  /**
   * 记录类型：0-操作记录；1-异常记录
   */
  private Integer recordType;
  /**
   * 操作类型 {@link OperationTypeEnum}
   */
  private Integer operationType;
  /**
   * 描述
   */
  private String description;
  /**
   * 请求方式 {@link RequestMethodEnum}
   */
  private String requestMethod;
  /**
   * 请求参数
   */
  private String requestParams;
  /**
   * 响应数据
   */
  private String responseData;
  /**
   * 请求耗时
   */
  private Long costTime;
  /**
   * 异常描述
   */
  private String exceptionDesc;
  /**
   * 请求头特征值
   */
  private String userAgent;
  /**
   * 操作时间
   */
  private Date operationTime;
  /**
   * 操作人ID
   */
  private String operationIp;
  /**
   * 操作人ID
   */
  private String operationUserId;
  /**
   * 操作姓名
   */
  private String operationUserName;
  /**
   * 租户ID
   */
  private String tenantId;

}
