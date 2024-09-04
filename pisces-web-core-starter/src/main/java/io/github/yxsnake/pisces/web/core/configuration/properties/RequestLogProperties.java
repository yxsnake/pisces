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
@ConfigurationProperties(prefix = "web-core.request-log")
public class RequestLogProperties {

    /**
     * 是否记录请求日志
     */
    private boolean needLogRequest = true;

    /**
     * 是否记录响应日志
     */
    private boolean needLogResponse = true;

    /**
     * 是否记录header
     */
    private boolean needLogHeader = true;

    /**
     * 是否记录参数
     */
    private boolean needLogPayload = true;

    /**
     * 记录的最大payload大小
     */
    private int maxPayloadLength = 2*1024*1024;


}
