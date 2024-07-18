package io.github.yxsnake.pisces.request.log.bean;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 请求日志
 *
 * @author Mayee
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 接收时间
   */
  private LocalDateTime receivedTime;
  /**
   * 请求url
   */
  String url;
  /**
   * 参数
   */
  Map<String, String[]> parameterMap;
  /**
   * 请求体
   */
  Object requestBody;
  /**
   * 请求类型 GET/POST/PUT/DELETE
   */
  String requestMethod;
  /**
   * 执行请求的方法
   */
  String methodName;
  /**
   * ip
   */
  String ip;

}
