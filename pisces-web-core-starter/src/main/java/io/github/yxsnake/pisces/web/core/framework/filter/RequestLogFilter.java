package io.github.yxsnake.pisces.web.core.framework.filter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.web.core.configuration.properties.RequestLogProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class RequestLogFilter  extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RequestLogProperties requestLogProperties;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 不进行过滤的请求pattern
     */
    private List<String> excludeUrlPatterns = new ArrayList<String>(Arrays.asList("/health"));

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String url = request.getServletPath();
        boolean matched = false;
        for (String pattern : excludeUrlPatterns) {
            matched = antPathMatcher.match(pattern, url);
            if (matched) {
                break;
            }
        }
        return matched;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Date requestDate = new Date();
        boolean isFirstRequest = !isAsyncDispatch(request);

        //包装缓存requestBody信息
        HttpServletRequest requestToUse = request;
        if (requestLogProperties.isNeedLogPayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, requestLogProperties.getMaxPayloadLength());
        }

        //包装缓存responseBody信息
        HttpServletResponse responseToUse = response;
        if (requestLogProperties.isNeedLogPayload() && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            //记录请求日志
            if (requestLogProperties.isNeedLogRequest()) {
                logRequest(requestToUse,requestDate);
            }
            //记录响应日志
            if (requestLogProperties.isNeedLogResponse()) {
                logResponse(responseToUse);
                //把从response中读取过的内容重新放回response，否则客户端获取不到返回的数据
                resetResponse(responseToUse);
            }
        }
    }

    /**
     * 记录请求日志
     * @param request
     * @param requestDate
     * @author
     * @date
     */
    protected void logRequest(HttpServletRequest request, Date requestDate) throws IOException {
        String payload = requestLogProperties.isNeedLogPayload() ? getRequestPayload(request) : "";
        logger.info(createRequestMessage(request, payload,requestDate));
    }

    /**
     * 记录响应日志
     * @param response
     */
    protected void logResponse(HttpServletResponse response) {
        String payload = requestLogProperties.isNeedLogPayload() ? getResponsePayload(response) : "";
        logger.info(createResponseMessage(response, payload, new Date()));
    }

    /**
     * 重新将响应参数设置到response中
     * @param response
     * @throws IOException
     */
    protected void resetResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.copyBodyToResponse();
        }
    }

    /**
     * 获取请求体中参数
     * @param request
     * @return
     */
    protected String getRequestPayload(HttpServletRequest request) throws IOException {
        String payload = "";
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            payload = getPayloadFromBuf(buf, wrapper.getCharacterEncoding());
        }
        return payload;
    }

    /**
     * 获取响应体中参数
     * @param response
     * @return
     */
    protected String getResponsePayload(HttpServletResponse response) {
        String payload = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            payload = getPayloadFromBuf(buf, wrapper.getCharacterEncoding());
        }
        return payload;
    }

    /**
     * 创建请求日志实际需要打印的内容
     * @param request
     * @param payload
     * @param requestDate
     * @return
     */
    protected String createRequestMessage(HttpServletRequest request, String payload, Date requestDate) {
        StringBuilder msg = new StringBuilder();
        msg.append("Inbound Message\n----------------------------\n");
        msg.append("Address: ").append(request.getRequestURL()).append("\n");
        msg.append("HttpMethod: ").append(request.getMethod()).append("\n");
        msg.append("QueryString: ").append(request.getQueryString()).append("\n");
        msg.append("RequestDate: ").append(DateUtil.format(requestDate, DatePattern.NORM_DATETIME_PATTERN)).append("\n");
        msg.append("Encoding: ").append(request.getCharacterEncoding()).append("\n");
        msg.append("Content-Type: ").append(request.getContentType()).append("\n");
        if (requestLogProperties.isNeedLogHeader()) {
            msg.append("Headers: ").append(new ServletServerHttpRequest(request).getHeaders()).append("\n");
        }
        if (requestLogProperties.isNeedLogPayload()) {
            int length = Math.min(payload.length(), requestLogProperties.getMaxPayloadLength());
            msg.append("Payload: ").append(payload.substring(0, length)).append("\n");
        }
        msg.append("----------------------------------------------");
        return msg.toString();
    }

    /**
     * 创建响应日志实际需要打印的内容
     * @param response
     * @param payload
     * @param responseDate
     * @return
     */
    protected String createResponseMessage(HttpServletResponse response, String payload, Date responseDate) {
        StringBuilder msg = new StringBuilder();
        msg.append("Outbound Message\n----------------------------\n");
        msg.append("ResponseDate: ").append(DateUtil.format(responseDate, DatePattern.NORM_DATETIME_PATTERN)).append("\n");
        msg.append("Encoding: ").append(response.getCharacterEncoding()).append("\n");
        msg.append("Content-Type: ").append(response.getContentType()).append("\n");
        if (requestLogProperties.isNeedLogHeader()) {
            msg.append("Headers: ").append(new ServletServerHttpResponse(response).getHeaders()).append("\n");
        }
        boolean needLogContentType = true;
        String contentType = response.getContentType();
//        //excel文件导出的不需要记录
//        if ("application/octet-stream;charset=UTF-8".equals(contentType)) {
//            needLogContentType = false;
//        }
        //是JSON格式的才输出
        needLogContentType = StrUtil.isEmpty(contentType) || contentType.toUpperCase().contains("JSON") || contentType.contains("text");
        if (requestLogProperties.isNeedLogPayload() && needLogContentType) {
            int length = Math.min(payload.length(), requestLogProperties.getMaxPayloadLength());
            msg.append("Payload: ").append(payload.substring(0, length)).append("\n");
        }
        msg.append("----------------------------------------------");
        return msg.toString();
    }

    /**
     * 将bytep[]参数转换为字符串用于输出
     * @param buf
     * @param characterEncoding
     * @return
     */
    protected String getPayloadFromBuf(byte[] buf, String characterEncoding) {
        String payload = "";
        if (buf.length > 0) {
            int length = Math.min(buf.length, requestLogProperties.getMaxPayloadLength());
            try {
                payload = new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return payload;
    }


    @Override
    public void destroy() {
        logger.info(">>>> RequestLogFilter destroy <<<<");
    }
}
