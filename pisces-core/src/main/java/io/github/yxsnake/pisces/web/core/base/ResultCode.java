package io.github.yxsnake.pisces.web.core.base;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author snake
 * @description
 * @since 2024/1/15 23:20
 */
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public enum ResultCode implements IResultCode, Serializable {

  SUCCESS(HttpServletResponse.SC_OK, "成功"),

  BAD_REQUEST(HttpServletResponse.SC_BAD_REQUEST,"请求参数有误"),

  MISSING_PATH_VARIABLE(HttpServletResponse.SC_BAD_REQUEST,"丢失PATH路径参数"),

  REQUEST_BINDING_ERROR(HttpServletResponse.SC_BAD_REQUEST,"绑定请求参数异常"),

  TYPE_MISMATCH_ERROR(HttpServletResponse.SC_BAD_REQUEST,"类型匹配错误"),

  METHOD_ARGUMENT_NOT_VALID(HttpServletResponse.SC_BAD_REQUEST,"无效的参数"),

  NOT_WRITABLE_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"对象不可操作"),

  JSON_FORMAT_ERROR(HttpServletResponse.SC_BAD_REQUEST,"JSON格式错误"),

  UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED,"请先进行认证"),

  FORBIDDEN(HttpServletResponse.SC_FORBIDDEN,"未获得授权，请联系管理员"),

  NOT_FOUND(HttpServletResponse.SC_NOT_FOUND,"请求资源不存在"),

  METHOD_NOT_ALLOWED(HttpServletResponse.SC_METHOD_NOT_ALLOWED,"请求方式不支持"),


  NOT_ACCEPTABLE(HttpServletResponse.SC_NOT_ACCEPTABLE,"不可接受该请求"),

  LENGTH_REQUIRED(HttpServletResponse.SC_NOT_ACCEPTABLE,"请求长度受限制"),

  UNSUPPORTED_MEDIA_TYPE(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,"不支持的媒体类型"),

  REQUESTED_RANGE_NOT_SATISFIABLE(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE,"无法处理所请求的数据区间"),

  INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"服务繁忙，稍后再试"),

  SERVICE_UNAVAILABLE(HttpServletResponse.SC_SERVICE_UNAVAILABLE,"服务不可用"),

  NOT_LOGIN(HttpServletResponse.SC_FORBIDDEN,"未登录授权"),

  NOT_PERMISSION(HttpServletResponse.SC_UNAUTHORIZED,"未授权,服务访问该资源"),

  SA_TOKEN_ERROR(HttpServletResponse.SC_BAD_REQUEST,"token错误"),

  ;

  @Override
  public Integer getCode() {
    return code;
  }

  @Override
  public String getMsg() {
    return msg;
  }

  private Integer code;

  private String msg;

  @Override
  public String toString() {
    return "{" +
      "\"code\":\"" + code + '\"' +
      ", \"msg\":\"" + msg + '\"' +
      '}';
  }


  public static ResultCode getValue(String code) {
    for (ResultCode value : values()) {
      if (value.getCode().equals(code)) {
        return value;
      }
    }
    // 默认系统执行错误
    return INTERNAL_SERVER_ERROR;
  }
}


