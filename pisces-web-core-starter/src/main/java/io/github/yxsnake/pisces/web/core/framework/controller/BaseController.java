package io.github.yxsnake.pisces.web.core.framework.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.base.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author snake
 * @description
 * @since 2024/1/16 22:01
 */
@SuppressWarnings("ALL")
@Component
public class BaseController {

  @Autowired
  protected HttpServletRequest request;

  @Autowired
  protected HttpServletResponse response;

  /**
   * 将日期格式字符串，自动转化为Date类型
   */
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    String[] disallowedFields = {"class.*", "Class.*", "*.class.*", "*.Class.*"};
    binder.setDisallowedFields(disallowedFields);
    // Date 类型转换
    binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(DateUtil.parseDate(text));
      }
    });
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(LocalDateTimeUtil.parseDate(text, dateFormatter));
      }
    });
    binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(LocalDateTimeUtil.parse(text, dateTimeFormatter));
      }
    });
    binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
      @Override
      public void setAsText(String text) {
        setValue(LocalTime.parse(text, timeFormatter));
      }
    });
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
  }

  /**
   * 页面跳转
   *
   * @param url 目标url
   * @return 跳转语句
   */
  protected String redirect(String url) {
    return StrUtil.format("redirect:{}", url);
  }

  /**
   * 成功返回
   *
   * @param object 返回对象
   * @param <T>    T
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<Result<T>> success(T object) {
    return success(object, null, HttpStatus.OK);
  }

  /**
   * 成功返回
   *
   * @return ResponseEntity
   */
  protected ResponseEntity success() {
    return success(HttpStatus.OK);
  }

  /**
   * 成功返回
   *
   * @param status HttpStatus对象
   * @param <T>    T
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<Result<T>> success(HttpStatus status) {
    return success(null, null, status);
  }

  /**
   * 成功返回
   *
   * @param object  返回对象
   * @param headers HttpHeaders对象
   * @param status  HttpStatus对象
   * @param <T>     T
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<Result<T>> success(T object, HttpHeaders headers, HttpStatus status) {
    if (headers == null) {
      headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
    }
    return new ResponseEntity<>(
      Result.<T>builder()
        .data(object)
        .code(ResultCode.SUCCESS.getCode())
        .msg(ResultCode.SUCCESS.getMsg())
        .build(), headers, status);
  }

  @SuppressWarnings("unchecked")
  protected <T> ResponseEntity directlyReturn(T object) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity(object, headers, HttpStatus.OK);
  }

  protected <T> ResponseEntity<Result<T>> fail(Result<T> result) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new ResponseEntity<>(result, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
