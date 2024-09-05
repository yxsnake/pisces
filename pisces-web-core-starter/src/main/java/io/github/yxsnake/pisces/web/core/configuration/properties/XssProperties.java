package io.github.yxsnake.pisces.web.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: snake
 * @create-time: 2024-09-05
 * @description:
 * @version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "web-core.xss")
public class XssProperties {

    private Boolean enabled = true;
}
