package com.github.snake.employee.model.form;

import io.github.yxsnake.pisces.web.core.converter.Convert;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Schema(description ="员工创建请求参数")
@ToString
public class CreateEmployeeForm implements Convert {


    @Schema(name = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @Schema(name = "性别")
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @Schema(name = "部门ID")
    @NotBlank(message = "部门不能为空")
    private String deptId;

    @Schema(name = "岗位")
    @NotBlank(message = "岗位不能为空")
    private String postId;

    @Schema(name = "入职日期")
    private Date entryDate;

}
