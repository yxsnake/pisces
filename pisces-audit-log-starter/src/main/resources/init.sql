DROP TABLE IF EXISTS `sys_audit_log`;
CREATE TABLE `sys_audit_log`
(
  `id`                  varchar(32) NOT NULL COMMENT '主键',
  `module_code`         varchar(32)   DEFAULT NULL COMMENT '模块编码',
  `module_name`         varchar(32)   DEFAULT NULL COMMENT '模块名称',
  `microservices_name`  varchar(32)   DEFAULT NULL COMMENT '微服务名称',
  `record_type`         smallint(3) DEFAULT NULL COMMENT '记录类型：0-操作记录；1-异常记录',
  `operation_type`      smallint(3) DEFAULT NULL COMMENT '操作类型：',
  `description`         varchar(32)   DEFAULT NULL COMMENT '操作描述',
  `request_method`      varchar(32)   DEFAULT NULL COMMENT '请求方法',
  `request_params`      varchar(32)   DEFAULT NULL COMMENT '请求方法参数',
  `response_data`       varchar(32)   DEFAULT NULL COMMENT '响应数据',
  `cost_time`           bigint(20) COMMENT '耗时（毫秒）',
  `exception_desc`      varchar(1000) DEFAULT NULL COMMENT '异常描述',
  `user_agent`          varchar(100)  DEFAULT NULL COMMENT '请求头特征值',
  `operation_time`      datetime COMMENT '操作时间',
  `operation_ip`        varchar(32)   DEFAULT NULL COMMENT '操作人IP',
  `operation_user_id`   varchar(32)   DEFAULT NULL COMMENT '操作人ID',
  `operation_user_name` varchar(32)   DEFAULT NULL COMMENT '操作人姓名',
  `tenant_id`           varchar(64)   DEFAULT NULL COMMENT '租户唯一标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='审计日志';
