package io.github.yxsnake.pisces.web.core.configuration;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.yxsnake.pisces.web.core.configuration.properties.WebCoreProperties;
import io.github.yxsnake.pisces.web.core.framework.handler.WebHandlerExceptionResolver;
import io.github.yxsnake.pisces.web.core.framework.handler.WebRequestMappingHandlerMapping;
import io.github.yxsnake.pisces.web.core.handler.UserContextInterceptor;
import io.github.yxsnake.pisces.web.core.spring.validator.ValidatorCollectionImpl;
import io.github.yxsnake.pisces.web.core.undertow.UndertowServerFactoryCustomizer;
import io.undertow.Undertow;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class WebCoreMvcAutoConfiguration implements WebMvcConfigurer, WebMvcRegistrations {

    private final WebCoreProperties webConf;

    @Override
    public Validator getValidator() {
        return new SpringValidatorAdapter(new ValidatorCollectionImpl());
    }

    @Override
    public void configureHandlerExceptionResolvers(
            List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new WebHandlerExceptionResolver(webConf));
    }

    @Bean
    @ConditionalOnClass(Undertow.class)
    public UndertowServerFactoryCustomizer undertowServerFactoryCustomizer() {
        return new UndertowServerFactoryCustomizer();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Set<String> excludePathPatterns = webConf.getExcludePathPatterns();
        if(CollUtil.isEmpty(excludePathPatterns)){
           excludePathPatterns = Sets.newHashSet();
        }
        excludePathPatterns.add("/user/permissions");
        excludePathPatterns.add("/user/roles");
        excludePathPatterns.add("/doc.html");
        excludePathPatterns.add("/webjars/**");
        excludePathPatterns.add("/v3/api-docs/*");
        excludePathPatterns.add("/favicon.ico");
        excludePathPatterns.add("/swagger-ui/**");
        List<String> excludePathList = Lists.newArrayList();
        if(CollUtil.isNotEmpty(excludePathPatterns)){
            excludePathList.addAll(excludePathPatterns);
        }
        registry.addInterceptor(new UserContextInterceptor())
                // 不用拦截的请求
                .excludePathPatterns(excludePathList)
                // 拦截的请求
                .addPathPatterns("/**")
        ;
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                if (!"GET".equalsIgnoreCase(request.getMethod()) || !request.getRequestURI().toString().equals("/favicon.ico")) {
                    return true;
                }
                response.setStatus(HttpStatus.NO_CONTENT.value()); // 设置状态码为204 No Content
                return false;
            }
        }).addPathPatterns("/**");
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new WebRequestMappingHandlerMapping(webConf.getVersion());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        //序列化的时候序列对象的所有属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DateFormat dateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        objectMapper.setDateFormat(dateFormat);

        //添加此配置
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
