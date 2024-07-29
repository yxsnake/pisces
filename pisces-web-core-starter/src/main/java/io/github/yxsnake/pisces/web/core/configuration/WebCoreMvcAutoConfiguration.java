package io.github.yxsnake.pisces.web.core.configuration;

import cn.hutool.core.collection.CollUtil;
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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

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

}
