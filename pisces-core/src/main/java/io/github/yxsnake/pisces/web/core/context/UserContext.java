package io.github.yxsnake.pisces.web.core.context;

import io.github.yxsnake.pisces.web.core.base.LoginUser;

import java.util.Objects;

/**
 * @author snake
 * @description
 * @since 2024/1/16 23:33
 */
public class UserContext {

  private final static ThreadLocal<LoginUser> CONTEXT = new ThreadLocal<>();

  private UserContext() {
  }

  public static void set(LoginUser loginUser) {
    CONTEXT.set(loginUser);
  }

  public static LoginUser get() {
    return Objects.isNull(CONTEXT.get())?new LoginUser():CONTEXT.get();
  }

  public static void remove() {
    CONTEXT.remove();
  }

  public static String getTenantId(){
    return get().getTenantId();
  }
  public static String getUserId(){
    return get().getUserId();
  }
  public static String getRealName(){
    return get().getRealName();
  }
  public static String getAccountId(){
    return get().getAccountId();
  }

  public static Integer getChannel(){
    return get().getChannel();
  }

  public static Boolean isSupperAdmin(){
    return get().getSupperAdmin();
  }
}
