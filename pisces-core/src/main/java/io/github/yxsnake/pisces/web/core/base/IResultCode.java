package io.github.yxsnake.pisces.web.core.base;

/**
 * @author snake
 * @description
 * @since 2024/1/15 23:18
 */
public interface IResultCode {

  Integer getCode();

  String getMsg();

  default ErrorCode convert() {
    return ErrorCode.builder()
      .code(getCode())
      .msg(getMsg())
      .build();
  }


  default ErrorCode overrideMsg(String msg) {
    return ErrorCode.builder()
      .code(getCode())
      .msg(msg)
      .build();
  }
}
