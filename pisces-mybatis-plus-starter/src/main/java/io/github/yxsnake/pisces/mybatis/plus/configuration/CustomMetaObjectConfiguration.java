package io.github.yxsnake.pisces.mybatis.plus.configuration;

import io.github.yxsnake.pisces.mybatis.plus.handler.MyMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomMetaObjectConfiguration {

    @Bean
    public MyMetaObjectHandler metaObjectHandler(){
        return new MyMetaObjectHandler();
    }

}
