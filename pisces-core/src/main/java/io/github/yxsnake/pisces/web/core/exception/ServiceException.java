package io.github.yxsnake.pisces.web.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author snake
 * @description 1.对于具体的业务异常继承此异常来实现，例如登录失败返回code码异常，则定义LoginFailException
 * 2.如果异常需要被外部服务感知到则进行抛出，否则内部拦截处理
 * @since 2024/1/15 22:44
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {


  private static final long serialVersionUID = 430933593095358673L;

  private String code;

  private String msg;


  /**
   * 构造新实例。
   */
  public ServiceException() {
    super();
  }

  /**
   * 用给定的异常信息构造新实例。
   *
   * @param msg 异常信息。
   */
  public ServiceException(String msg) {
    super((String) null);
    this.msg = msg;
  }

  /**
   * 用表示异常原因的对象构造新实例。
   *
   * @param cause 异常原因。
   */
  public ServiceException(Throwable cause) {
    super(null, cause);
  }

  /**
   * 用异常消息和表示异常原因的对象构造新实例。
   *
   * @param msg   异常信息。
   * @param cause 异常原因。
   */
  public ServiceException(String msg, Throwable cause) {
    super(null, cause);
    this.msg = msg;
  }

  /**
   * 用异常消息和表示异常原因及其他信息的对象构造新实例。
   *
   * @param msg   异常信息。
   * @param code  错误代码。
   * @param cause 异常原因。
   */
  public ServiceException(String code, String msg, Throwable cause) {
    this(msg, cause);
    this.code = code;
  }

  /**
   * 返回异常信息。
   *
   * @return 异常信息。
   */
  public String getErrorMsg() {
    return msg;
  }

  /**
   * 返回错误代码的字符串表示。
   *
   * @return 错误代码的字符串表示。
   */
  public String getErrorCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return getMsg();
  }

  public ServiceException(String code, String msg) {
    super(msg);
    this.code = code;
  }
}
