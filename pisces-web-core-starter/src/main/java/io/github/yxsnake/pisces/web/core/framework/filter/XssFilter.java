package io.github.yxsnake.pisces.web.core.framework.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.github.yxsnake.pisces.web.core.framework.wrapper.XssRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author: snake
 * @create-time: 2024-07-04
 * @description: xss过滤器
 * @version: 1.0
 */
public class XssFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        XssRequestWrapper wrappedRequest = new XssRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);
    }
}
