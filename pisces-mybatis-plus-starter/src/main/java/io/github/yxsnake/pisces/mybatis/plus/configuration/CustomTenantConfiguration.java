package io.github.yxsnake.pisces.mybatis.plus.configuration;

import lombok.RequiredArgsConstructor;
import io.github.yxsnake.pisces.mybatis.plus.configuration.properties.TenantProperties;
import io.github.yxsnake.pisces.mybatis.plus.handler.CustomTenantHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(TenantProperties.class)
@Configuration
@RequiredArgsConstructor
public class CustomTenantConfiguration {

    private final TenantProperties tenantProperties;

    @Bean
    public CustomTenantHandler customTenantHandler(){
        return new CustomTenantHandler(tenantProperties);
    }
}
