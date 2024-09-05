package io.github.yxsnake.pisces.web.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "web-core.swagger")
public class SwaggerProperties {

    private Boolean enabled;

    private String title;

    private String author;

    private String email;

    private String description;

    private String url;

    private String version;

}
