package io.github.yxsnake.pisces.web.core.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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

  private String nickname;

  private String avatar;

  private String phone;

  private String email;

  private Boolean supperAdmin = Boolean.FALSE;

  private String tenantId;

  /**
   * 部门ID
   */
  private Long deptId;

  /**
   * 部门名
   */
  private String deptName;

  /**
   * 角色对象
   */
  private List<RoleDTO> roles;

  /**
   * 设备类型
   */
  private String deviceType;

  /**
   * 登录IP地址
   */
  private String ipaddr;

  /**
   * 登录地点
   */
  private String loginLocation;

  /**
   * 浏览器类型
   */
  private String browser;

  /**
   * 操作系统
   */
  private String os;

  /**
   * 菜单权限
   */
  private Set<String> menuPermission;

  /**
   * 角色权限
   */
  private Set<String> rolePermission;

  /**
   * 数据权限 当前角色ID
   */
  private Long roleId;

  private String accountType;


  public String getLoginId() {
    if (accountType == null) {
      throw new IllegalArgumentException("账号类型不能为空");
    }
    if (userId == null) {
      throw new IllegalArgumentException("用户ID不能为空");
    }
    return accountType + ":" + userId;
  }

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
