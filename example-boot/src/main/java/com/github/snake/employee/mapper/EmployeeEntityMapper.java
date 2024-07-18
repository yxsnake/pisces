package com.github.snake.employee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.snake.employee.model.entity.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeEntityMapper extends BaseMapper<EmployeeEntity> {

}
