package io.github.yxsnake.pisces.request.log.bean;

import lombok.*;

import java.io.Serializable;

/**
 * 扩展信息
 *
 * @author Mayee
 */
@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ExpandMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 执行请求的方法
   */
  String methodName;

}
