package io.github.yxsnake.pisces.web.core.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.Method;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author snake
 * @description
 * @since 2024/1/15 23:13
 */
@Slf4j
public class RequestUtils {

  public static String getHeader(String key) {
    return getRequest().getHeader(key);
  }

  /**
   * 判断请求方式GET
   */
  public static boolean isGet(HttpServletRequest request) {
    return Method.GET.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式POST
   */
  public static boolean isPost(HttpServletRequest request) {
    return Method.POST.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式PUT
   */
  public static boolean isPut(HttpServletRequest request) {
    return Method.PUT.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式DELETE
   */
  public static boolean isDelete(HttpServletRequest request) {
    return Method.DELETE.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式PATCH
   */
  public static boolean isPatch(HttpServletRequest request) {
    return Method.PATCH.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式TRACE
   */
  public static boolean isTrace(HttpServletRequest request) {
    return Method.TRACE.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式HEAD
   */
  public static boolean isHead(HttpServletRequest request) {
    return Method.HEAD.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 判断请求方式OPTIONS
   */
  public static boolean isOptions(HttpServletRequest request) {
    return Method.OPTIONS.toString().equalsIgnoreCase(request.getMethod());
  }

  /**
   * 是否包含请求体
   */
  public static boolean isContainBody(HttpServletRequest request) {
    return isPost(request) || isPut(request) || isPatch(request);
  }


  public static ServletRequestAttributes getRequestAttributes() {
    try {
      RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
      return (ServletRequestAttributes) attributes;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 获取request
   */
  public static HttpServletRequest getRequest() {
    try {
      return getRequestAttributes().getRequest();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 获取response
   */
  public static HttpServletResponse getResponse() {
    try {
      return getRequestAttributes().getResponse();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * 获取http请求的Domain
   *
   * @param request
   * @return
   */
  public static String getDomain(HttpServletRequest request) {
    StringBuffer url = request.getRequestURL();
    String contextPath = request.getServletContext().getContextPath();
    return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
  }

  /**
   * 获取请求
   */
  public static byte[] getByteBody(HttpServletRequest request) {
    byte[] body = new byte[0];
    try {
      body = StreamUtils.copyToByteArray(request.getInputStream());
    } catch (IOException e) {
      log.error("Error: Get RequestBody byte[] fail," + e);
    }
    return body;
  }

  /**
   * 获取请求
   */
  public static String getRequestBody(HttpServletRequest request) {
    String requestBody = null;
    if (isContainBody(request)) {
      try {
        ServletInputStream inputStream = null;
        inputStream = request.getInputStream();
        if (Objects.nonNull(inputStream)) {
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          IoUtil.copy(inputStream, bos);
          byte[] buff = bos.toByteArray();
          requestBody = new String(buff);
        }
      } catch (IOException ignored) {
      }
    }
    return requestBody;
  }

  public static String API_REST_RESULT = "API_REST_RESULT";

  /**
   * 是否是Ajax异步请求
   *
   * @param request
   */
  public static boolean isAjaxRequest(HttpServletRequest request) {
    Boolean isRestResult = TypeUtils.castToBoolean(request.getAttribute(API_REST_RESULT));
    if (Objects.nonNull(isRestResult)) {
      return isRestResult;
    }
    String accept = request.getHeader("accept");
    if (accept != null && accept.contains("application/json")) {
      return true;
    }

    String xRequestedWith = request.getHeader("X-Requested-With");
    if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
      return true;
    }

    String uri = request.getRequestURI();
    if (uri.contains(".json") || uri.contains(".xml")) {
      return true;
    }

    String ajax = request.getParameter("__ajax");
    if ("json".equalsIgnoreCase(ajax) || "xml".equalsIgnoreCase(ajax)) {
      return true;
    }

    return false;
  }

}

