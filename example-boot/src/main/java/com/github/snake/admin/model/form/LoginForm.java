package com.github.snake.admin.model.form;

import lombok.Data;

@Data
public class LoginForm {

  private String account;
  private String pwdCipherText;
  private Integer isRememberMe;
}
