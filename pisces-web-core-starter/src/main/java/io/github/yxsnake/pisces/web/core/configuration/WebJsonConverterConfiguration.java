package io.github.yxsnake.pisces.web.core.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author: snake
 * @create-time: 2024-06-26
 * @description: 时间序列化
 * @version: 1.0
 */
@Configuration
public class WebJsonConverterConfiguration implements WebMvcConfigurer {

    /**
     * 解决Java8 日期序列化问题
     * jackson默认不支持java8 LocalDateTime的序列化和反序列化
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        messageConverter.setObjectMapper(objectMapper);
        converters.add(0, messageConverter);
        //需要追加byte，否则springdoc-openapi接口会响应Base64编码内容，导致接口文档显示失败
        // https://github.com/springdoc/springdoc-openapi/issues/2143
        // 解决方案
        converters.add(1,new ByteArrayHttpMessageConverter());
    }
}
