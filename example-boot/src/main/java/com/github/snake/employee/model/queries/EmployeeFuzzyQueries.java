package com.github.snake.employee.model.queries;

import lombok.Data;

@Data
public class EmployeeFuzzyQueries {

    private String realName;

    public static EmployeeFuzzyQueries create(){
        return new EmployeeFuzzyQueries();
    }
}
