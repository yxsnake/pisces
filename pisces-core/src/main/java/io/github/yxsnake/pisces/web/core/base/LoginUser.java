package io.github.yxsnake.pisces.web.core.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author snake
 * @description
 * @since 2024/1/16 23:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

  private String userId;

  private String accountId;

  private Integer channel;

  private String realName;

  private String avatar;

  private String phone;

  private String email;

  private Boolean supperAdmin = Boolean.FALSE;

  private String tenantId;


  public static LoginUser defaultUser(){
    LoginUser loginUser = new LoginUser();
    loginUser.setUserId("1");
    loginUser.setEmail("370696614@qq.com");
    loginUser.setChannel(11);
    loginUser.setRealName("张三");
    loginUser.setPhone("18512341234");
    loginUser.setTenantId("999999");
    loginUser.setAccountId("1");
    loginUser.setAvatar("https://i1.hdslb.com/bfs/face/98a570a6c6d6a263bcb0cba9e15e492125e9d310.jpg@120w_120h_1c");
    return loginUser;
  }
}
