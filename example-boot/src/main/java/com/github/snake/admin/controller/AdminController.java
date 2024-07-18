package com.github.snake.admin.controller;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.github.snake.admin.model.entity.SysUser;
import com.github.snake.admin.model.form.LoginForm;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.framework.controller.BaseController;
import com.google.common.base.Throwables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class AdminController extends BaseController {

  //TODO 系统登录和登出
  //TODO 改为POST请求
  //登录
  @PostMapping("/login")
  public Result login(@RequestBody LoginForm form) {
    //TODO 可以加上登录失败次数校验,错误次数存redis中
    //TODO 密码加密,这里使用MD5加密,后台分配管理员设置账号时需要存储明文和加密密文,这里取的是加密密文对比
    //TODO 或者可以加入一个生成验证码验证,提供一个获取验证码的接口给前端,生成后输入验证码验证登录,可以防止接口被刷的风险
    if (StrUtil.isBlank(form.getAccount()) || StrUtil.isBlank(form.getPwdCipherText())) {
      return Result.failed(500, "登录账号或密码不为空!");
    }
    try {
      //TODO 根据account、Md5加密密码pwdCipherText查询User/admin
      SysUser user = this.findSysUser(form);
      if (Objects.isNull(user)) {
        return Result.failed(400, "登录账号不存在!");
      }
      if ((StrUtil.isNotBlank(user.getAccount()) && !user.getAccount().equals(form.getAccount()))
        || (StrUtil.isNotBlank(user.getCipherText()) && !user.getCipherText().equals(form.getPwdCipherText()))) {
        return Result.failed(400, "登录账号或密码不为正确!");
      }
      // 记住我--->`SaLoginModel`为登录参数Model，其有诸多参数决定登录时的各种逻辑
      SaLoginModel saLoginModel = SaLoginConfig
        .setDevice("PC")                // 此次登录的客户端设备类型, 用于[同端互斥登录]时指定此次登录的设备类型
        .setIsLastingCookie(true)        // 是否为持久Cookie（临时Cookie在浏览器关闭时会自动删除，持久Cookie在重新打开后依然存在）
        .setIsWriteHeader(false);       // 是否在登录后将 Token 写入到响应头
      if (Objects.nonNull(form.getIsRememberMe()) && form.getIsRememberMe().intValue() == 1) {
        // 指定此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的 timeout 值）,全局的timeout设置的是1天,记住我设置的是7天
        saLoginModel.setTimeout(60 * 60 * 24 * 7);
      }
      //加入权限和角色
      List<String> roleList = StpUtil.getRoleList(user.getId());
      saLoginModel.setExtra("roles", roleList);
      List<String> permissionList = StpUtil.getPermissionList(user.getId());
      saLoginModel.setExtra("permissions", permissionList);
      //这里的id是admin的id主键
      StpUtil.login(user.getId(), saLoginModel);
      SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
      return Result.success(tokenInfo);
    } catch (Exception e) {
      log.error("企业会员外部系统登录异常:{}", Throwables.getStackTraceAsString(e));
      if (StrUtil.isBlank(e.getMessage())) {
        return Result.failed(400, "登录失败");
      }
      return Result.failed(400, "登录失败:" + e.getMessage());
    }
  }

  private SysUser findSysUser(LoginForm form) {
      return SysUser.builder()
        .id("1")
        .account("admin")
        .cipherText("admin")
        .build();
  }


  /**
   * 查询登录状态 请求头带上login的token的值 Bearer xxx
   *
   * @return
   */
  @GetMapping("/isLogin")
  public Result isLogin() {
    return Result.success("是否登录：" + StpUtil.isLogin());
  }

  /**
   * 注销 请求头带上login的token的值 Bearer xxx
   *
   * @return
   */
  @PostMapping("/logout")
  public Result logout() {
    StpUtil.logout();
    return Result.success();
  }

}
