package io.github.yxsnake.pisces.web.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author snake
 * @description 线程池任务异常
 * @since 2024/1/15 22:46
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class ThreadPoolExecutorException extends ServiceException {

  /**
   * 用表示异常原因的对象构造新实例。
   *
   * @param cause 异常原因。
   */
  public ThreadPoolExecutorException(Throwable cause) {
    super(null, cause);
  }

}
