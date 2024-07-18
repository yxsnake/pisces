package com.github.snake.employee.model.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.github.yxsnake.pisces.web.core.framework.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class EmployeeDTO extends BaseEntity implements Convert {

    @Schema(name = "用户ID")
    private Long id;

    @Schema(name = "真实姓名")
    private String realName;

    @Schema(name = "性别")
    private Integer gender;

    @Schema(name = "部门ID")
    private String deptId;

    @Schema(name = "岗位")
    private String postId;

    @Schema(name = "入职日期")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private Date entryDate;

    @Schema(name = "员工状态")
    private Integer status;
}
