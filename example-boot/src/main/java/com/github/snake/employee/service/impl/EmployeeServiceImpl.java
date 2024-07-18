package com.github.snake.employee.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yxsnake.pisces.web.core.base.QueryFilter;
import com.github.snake.employee.mapper.EmployeeEntityMapper;
import com.github.snake.employee.model.dto.EmployeeDTO;
import com.github.snake.employee.model.entity.EmployeeEntity;
import com.github.snake.employee.model.form.CreateEmployeeForm;
import com.github.snake.employee.model.queries.EmployeeFuzzyQueries;
import com.github.snake.employee.model.queries.EmployeeQueries;
import com.github.snake.employee.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.github.yxsnake.pisces.web.core.enums.StatusEnum;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends ServiceImpl<EmployeeEntityMapper, EmployeeEntity> implements IEmployeeService {


  private final RocketMQTemplate rocketMQTemplate;
  @Override
  public EmployeeDTO create(CreateEmployeeForm form) {
    EmployeeEntity employeeEntity = form.convert(EmployeeEntity.class);
    employeeEntity.setId(IdUtil.getSnowflakeNextId());
    employeeEntity.setStatus(StatusEnum.ENABLE.getValue());
    if (Objects.isNull(form.getEntryDate())) {
      employeeEntity.setEntryDate(DateUtil.date());
    }
    this.save(employeeEntity);
    // 发送消息
    String topicName = "employee";
    String messageBody = JsonUtils.objectCovertToJson(employeeEntity);
//    rocketMQTemplate.convertAndSend(topicName, messageBody);
    return employeeEntity.convert(EmployeeDTO.class);
  }

  @Override
  public List<EmployeeDTO> queryList(QueryFilter<EmployeeQueries, EmployeeFuzzyQueries> queryFilter) {
    return this.lambdaQuery().list().stream().map(employeeEntity -> employeeEntity.convert(EmployeeDTO.class)).collect(Collectors.toList());
  }

  @Override
  public EmployeeDTO get(Long id) {
    EmployeeEntity employeeEntity = this.getById(id);
    if (Objects.isNull(employeeEntity)) {
      return null;
    }
    return employeeEntity.convert(EmployeeDTO.class);
  }

  @Override
  public IPage<EmployeeDTO> queryPage(QueryFilter<EmployeeQueries, EmployeeFuzzyQueries> queryFilter) {
    // 等值查询条件
    EmployeeQueries equalsQueries = Objects.isNull(queryFilter.getEqualsQueries()) ? EmployeeQueries.create() : queryFilter.getEqualsQueries();
    // 模糊查询条件
    EmployeeFuzzyQueries fuzzyQueries = Objects.isNull(queryFilter.getEqualsQueries()) ? EmployeeFuzzyQueries.create() : queryFilter.getFuzzyQueries();
    // 分页查询
    return this.page(
                // 分页参数对象
                new Page<>(queryFilter.getPageNum(), queryFilter.getPageSize()),
                // 查询条件拼接
                Wrappers.lambdaQuery(EmployeeEntity.class)
                  .eq(Objects.nonNull(equalsQueries.getStatus()), EmployeeEntity::getStatus, equalsQueries.getStatus())
                  .like(Objects.nonNull(fuzzyQueries.getRealName()), EmployeeEntity::getRealName, equalsQueries.getRealName())
//                  .orderBy(true, false, EmployeeEntity::getCreateTime)
       )
      // 转为数据传输对象DTO
      .convert(employeeEntity -> employeeEntity.convert(EmployeeDTO.class));
  }

}
