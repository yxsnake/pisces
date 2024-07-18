package io.github.yxsnake.pisces.web.core.framework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author snake
 * @description
 * @since 2024/1/16 21:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthVO {

  private String applicationName;

  private String currentDate;

}
