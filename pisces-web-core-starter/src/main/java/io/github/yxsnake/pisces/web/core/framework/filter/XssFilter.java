package io.github.yxsnake.pisces.web.core.framework.filter;

import io.github.yxsnake.pisces.web.core.configuration.properties.XssProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.github.yxsnake.pisces.web.core.framework.wrapper.XssRequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author: snake
 * @create-time: 2024-07-04
 * @description: xss过滤器
 * @version: 1.0
 */

@Slf4j
@RequiredArgsConstructor
public class XssFilter extends OncePerRequestFilter {

    private final XssProperties xssProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(xssProperties.getEnabled()){
            XssRequestWrapper wrappedRequest = new XssRequestWrapper(request);
            filterChain.doFilter(wrappedRequest.getRequest(), response);
        }else{
            filterChain.doFilter(request,response);
        }
    }
}
