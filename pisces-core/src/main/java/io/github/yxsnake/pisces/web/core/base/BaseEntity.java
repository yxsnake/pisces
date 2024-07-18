package io.github.yxsnake.pisces.web.core.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author snake
 * @description 抽取创建时间和修改时间字段
 * @since 2024/1/15 21:58
 */
@Data
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = IdWorker.getId();

  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 创建人 ID
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String createUserId;

  /**
   * 创建人名称
   */
  @TableField(fill = FieldFill.INSERT)
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String createUserName;

  /**
   * 修改时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  /**
   * 修改人 ID
   */
  @TableField(fill = FieldFill.UPDATE)
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String updateUserId;

  /**
   * 修改人名称
   */
  @TableField(fill = FieldFill.UPDATE)
  @JsonInclude(value = JsonInclude.Include.NON_NULL)
  private String updateUserName;

}

