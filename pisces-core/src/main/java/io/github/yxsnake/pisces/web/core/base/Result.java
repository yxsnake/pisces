package io.github.yxsnake.pisces.web.core.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author snake
 * @description
 * @since 2024/1/15 23:19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

  @Serial
  private static final long serialVersionUID = 2787872966749171251L;

  public static final Integer SUCCESS = 200;

  private Integer code;

  private String msg;

  private T data;

  public static <T> Result<T> success() {
    return success(null);
  }

  public static <T> Result<T> success(T data) {
    Result<T> result = new Result<>();
    result.setCode(ResultCode.SUCCESS.getCode());
    result.setMsg(ResultCode.SUCCESS.getMsg());
    result.setData(data);
    return result;
  }

  public static <T> Result<T> failed() {
    return result(ResultCode.INTERNAL_SERVER_ERROR.getCode(), ResultCode.INTERNAL_SERVER_ERROR.getMsg(), null);
  }

  public static <T> Result<T> failed(String msg) {
    return result(ResultCode.INTERNAL_SERVER_ERROR.getCode(), msg, null);
  }

  public static <T> Result<T> failed(Integer code,String msg) {
    return result(code, msg, null);
  }

  public static <T> Result<T> judge(boolean status) {
    if (status) {
      return success();
    } else {
      return failed();
    }
  }

  public static <T> Result<T> failed(IResultCode resultCode) {
    return result(resultCode.getCode(), resultCode.getMsg(), null);
  }

  public static <T> Result<T> failed(IResultCode resultCode, String msg) {
    return result(resultCode.getCode(), msg, null);
  }

  private static <T> Result<T> result(IResultCode resultCode, T data) {
    return result(resultCode.getCode(), resultCode.getMsg(), data);
  }

  private static <T> Result<T> result(Integer code, String msg, T data) {
    Result<T> result = new Result<>();
    result.setCode(code);
    result.setData(data);
    result.setMsg(msg);
    return result;
  }

  public static boolean isSuccess(Result<?> result) {
    return result != null && ResultCode.SUCCESS.getCode().equals(result.getCode());
  }
}


