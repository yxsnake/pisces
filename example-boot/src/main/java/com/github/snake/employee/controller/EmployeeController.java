package com.github.snake.employee.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;
import io.github.yxsnake.pisces.web.core.base.Result;
import com.github.snake.employee.model.dto.EmployeeDTO;
import com.github.snake.employee.model.form.CreateEmployeeForm;
import com.github.snake.employee.model.queries.EmployeeFuzzyQueries;
import com.github.snake.employee.model.queries.EmployeeQueries;
import com.github.snake.employee.service.IEmployeeService;
import io.github.yxsnake.pisces.request.log.annotation.RequestLog;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestLog
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/employee")
public class EmployeeController extends BaseController {

    private final IEmployeeService employeeService;

    @Operation(description = "创建员工")
    @PostMapping(value = "create")
//    @AuditLog(description = "创建员工，参数=> 真实姓名：#{[0].realName}，性别：#{[0].gender}",microservicesName = "example-boot",module = LogModuleEnum.EMP,type = OperationTypeEnum.INSERT)
    public ResponseEntity<Result<EmployeeDTO>> create(@Validated @RequestBody CreateEmployeeForm form){
        return success(employeeService.create(form));
    }

    @Operation(description = "查询员工列表")
    @PostMapping(value = "queryList")
    public ResponseEntity<Result<List<EmployeeDTO>>> queryList(@RequestBody QueryFilter<EmployeeQueries, EmployeeFuzzyQueries> aggregateQueries){
        return success(employeeService.queryList(aggregateQueries));
    }
    @Operation(description = "分页查询员工")
    @PostMapping(value = "queryPage")
    public ResponseEntity<Result<IPage<EmployeeDTO>>> queryPage(@RequestBody QueryFilter<EmployeeQueries, EmployeeFuzzyQueries> aggregateQueries){
        return success(employeeService.queryPage(aggregateQueries));
    }


  @Operation(description = "查询员工详情")
    @GetMapping(value = "/getById")
    public ResponseEntity<Result<EmployeeDTO>> getById(@RequestParam("id") Long id){
      return success(employeeService.get(id));
    }
}
