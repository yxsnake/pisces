package io.github.yxsnake.pisces.web.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author snake
 * @description
 * @since 2024/1/16 21:50
 */
@Component
public class ApplicationContextRegister implements ApplicationContextAware {

  private static ApplicationContext APPLICATION_CONTEXT;

  static ApplicationContext getApplicationContext() {
    return APPLICATION_CONTEXT;
  }

  /**
   * 设置spring上下文
   *
   * @param applicationContext spring上下文
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    APPLICATION_CONTEXT = applicationContext;
  }
}

