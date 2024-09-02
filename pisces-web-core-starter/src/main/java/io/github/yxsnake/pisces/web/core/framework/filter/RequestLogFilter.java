package io.github.yxsnake.pisces.web.core.framework.filter;

import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.web.core.framework.wrapper.RequestLogWrapper;
import io.github.yxsnake.pisces.web.core.framework.wrapper.ResponseLogWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
//@WebFilter(filterName = "requestLogFilter",urlPatterns = {"/*"},
//        initParams = {@WebInitParam(name = "ignoredUrl", value = ".css;.js;.jpg;.png;.gif;.ico;.html"),
//                @WebInitParam(name = "filterPath", value = "/user/login#/user/registerUser")})
public class RequestLogFilter  extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        boolean writeLog = true;
        String ignoreLog = "swagger;upload;.jpg;.png;.zip;.dat;.ico;.pdf;download"; //可以放配置文件
        RequestLogWrapper requestWapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = servletRequest;
            for (String item : ignoreLog.split(";")) {
                if ("/".equals(request.getRequestURI()) || request.getRequestURI().toLowerCase().contains(item)) {
                    //有一个包含就不记
                    writeLog = false;
                    break;
                }
            }
            if (writeLog) {
                requestWapper = new RequestLogWrapper(request);
            }
        }
        ResponseLogWrapper responseLogWrapper = new ResponseLogWrapper(servletResponse);
        //获取请求中的流如何，将取出来的字符串，再次转换成流，然后把它放入到新request对象中
        // 在chain.doFiler方法中传递新的request对象
        if (requestWapper == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            if (writeLog) {
                filterChain.doFilter(requestWapper, responseLogWrapper);
            } else {
                filterChain.doFilter(requestWapper, servletResponse);
            }
        }
        if (writeLog) {
            //打印返回响应日志
            String result = new String(responseLogWrapper.getResponseData());
            ServletOutputStream outputStream = servletResponse.getOutputStream();
            outputStream.write(result.getBytes());
            outputStream.flush();
            outputStream.close();
            String queryStr = StrUtil.isEmpty(servletRequest.getQueryString()) ? "" : "?" + servletRequest.getQueryString();
            logger.info("Response => {}{} \r\n{}", servletRequest.getRequestURI(), queryStr, result);
        }
    }
    @Override
    public void destroy() {
        logger.info(">>>> RequestLogFilter destroy <<<<");
    }
}
