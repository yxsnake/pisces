package io.github.yxsnake.pisces.web.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: snake
 * @create-time: 2024-09-04
 * @description:
 * @version: 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "web-core.log")
public class RequestLogProperties {

    private boolean enabled = true;

    /**
     * 是否记录请求日志
     */
    private Boolean needLogRequest = true;

    /**
     * 是否记录响应日志
     */
    private Boolean needLogResponse = true;

    /**
     * 是否记录header
     */
    private Boolean needLogHeader = true;

    /**
     * 是否记录参数
     */
    private Boolean needLogPayload = true;

    /**
     * 记录的最大payload大小
     */
    private Integer maxPayloadLength = 2*1024*1024;


}
