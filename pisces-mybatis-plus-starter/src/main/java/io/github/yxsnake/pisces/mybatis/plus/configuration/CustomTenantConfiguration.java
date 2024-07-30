package io.github.yxsnake.pisces.mybatis.plus.configuration;

import io.github.yxsnake.pisces.mybatis.plus.configuration.properties.MybatisPlusExtProperties;
import lombok.RequiredArgsConstructor;
import io.github.yxsnake.pisces.mybatis.plus.handler.CustomTenantHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(MybatisPlusExtProperties.class)
@Configuration
@RequiredArgsConstructor
public class CustomTenantConfiguration {

    private final MybatisPlusExtProperties mybatisPlusExtProperties;

    @Bean
    public CustomTenantHandler customTenantHandler(){
        return new CustomTenantHandler(mybatisPlusExtProperties);
    }
}
