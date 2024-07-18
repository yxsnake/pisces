package com.github.snake.employee.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.yxsnake.pisces.web.core.converter.Convert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@TableName(value = "employee")
@Data
@ToString
@Accessors(chain = true)
@SuperBuilder
@AllArgsConstructor
public class EmployeeEntity implements Convert {

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 岗位
     */
    private String postId;
    /**
     * 入职日期
     */
    private Date entryDate;
    /**
     * 员工状态
     */
    private Integer status;
}
