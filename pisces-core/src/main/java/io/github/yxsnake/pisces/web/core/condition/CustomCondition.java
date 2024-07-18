package io.github.yxsnake.pisces.web.core.condition;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

/**
 * @author snake
 * @description
 * @since 2024/1/15 22:41
 */
public class CustomCondition implements Condition {

  private final String key;
  private final String value;

  public CustomCondition(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public boolean matches(@NonNull ConditionContext context,
                         @NonNull AnnotatedTypeMetadata metadata) {
    String beanName = context.getEnvironment().getProperty(key);
    return StrUtil.equalsIgnoreCase(beanName, value);
  }
}
