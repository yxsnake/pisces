package com.github.snake.employee.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;
import com.github.snake.employee.model.dto.EmployeeDTO;
import com.github.snake.employee.model.entity.EmployeeEntity;
import com.github.snake.employee.model.form.CreateEmployeeForm;
import com.github.snake.employee.model.queries.EmployeeFuzzyQueries;
import com.github.snake.employee.model.queries.EmployeeQueries;

import java.util.List;

public interface IEmployeeService extends IService<EmployeeEntity> {

  EmployeeDTO create(CreateEmployeeForm form);

  IPage<EmployeeDTO> queryPage(QueryFilter<EmployeeQueries, EmployeeFuzzyQueries> queryFilter);

  List<EmployeeDTO> queryList(QueryFilter<EmployeeQueries, EmployeeFuzzyQueries> queryFilter);

  EmployeeDTO get(Long id);

}
