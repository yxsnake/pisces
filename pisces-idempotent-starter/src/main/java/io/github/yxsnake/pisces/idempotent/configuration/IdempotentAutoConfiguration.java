package io.github.yxsnake.pisces.idempotent.configuration;

import io.github.yxsnake.pisces.idempotent.aspectj.RepeatSubmitAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;

/**
 * @author snake
 * @description
 * @since 2024/9/24 0:43
 */

@AutoConfiguration(after = RedisConfiguration.class)
public class IdempotentAutoConfiguration {

    @Bean
    public RepeatSubmitAspect repeatSubmitAspect() {
        return new RepeatSubmitAspect();
    }

}
