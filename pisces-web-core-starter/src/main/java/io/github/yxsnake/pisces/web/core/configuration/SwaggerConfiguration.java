package io.github.yxsnake.pisces.web.core.configuration;

import io.github.yxsnake.pisces.web.core.configuration.properties.SwaggerProperties;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final SwaggerProperties swaggerProperties;
    @Bean
    @ConditionalOnProperty(value = "web-core.swagger.enabled", havingValue = "true", matchIfMissing = true)
    public OpenAPI swaggerOpenAPI(){
        return new OpenAPI()
                .info(new Info().title(swaggerProperties.getTitle())
                        // 信息
                        .contact(new Contact()
                                .name(swaggerProperties.getAuthor())
                                .email(swaggerProperties.getEmail())
                                .url(swaggerProperties.getUrl())
                        )
                        // 简介
                        .description(swaggerProperties.getDescription())
                        // 版本
                        .version(swaggerProperties.getVersion())
                        // 许可证
                        .license(new License().name("Apache 2.0").url(swaggerProperties.getUrl())))
                .externalDocs(new ExternalDocumentation()
                        .description(swaggerProperties.getDescription())
                        .url(swaggerProperties.getUrl()));
    }
}