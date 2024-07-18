package io.github.yxsnake.pisces.web.core.base;

import lombok.*;

/**
 * @author snake
 * @description
 * @since 2024/1/15 23:18
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ErrorCode {

  /**
   * 错误码
   */
  private Integer code;
  /**
   * 错误消息
   */
  private String msg;
}
