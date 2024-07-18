package io.github.yxsnake.pisces.web.core.exception;

import io.github.yxsnake.pisces.web.core.base.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import io.github.yxsnake.pisces.web.core.base.Result;

@Getter
public class BizException extends RuntimeException {

  public ErrorCode errorCode;

  public BizException(ErrorCode errorCode) {
    super(errorCode.getMsg());
    this.errorCode = errorCode;
  }

  public BizException(String message) {
    super(message);
    this.errorCode = ErrorCode.builder()
      .code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
      .msg(message)
      .build();
  }

  public BizException(Integer code, String message) {
    ErrorCode errorCode = ErrorCode.builder()
      .code(code)
      .msg(message)
      .build();
    this.errorCode = errorCode;
  }

  public BizException(String message, Throwable cause) {
    super(message, cause);
  }

  public BizException(Throwable cause) {
    super(cause);
  }

  public Result getResult(){
    return Result.builder()
            .code(this.errorCode.getCode())
            .msg(this.errorCode.getMsg())
            .build();
  }
}


