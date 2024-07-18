package com.github.snake.employee.model.queries;

import lombok.Data;

@Data
public class EmployeeQueries {

    private Integer status;

    private String realName;

    public static EmployeeQueries create(){
        return new EmployeeQueries();
    }
}
