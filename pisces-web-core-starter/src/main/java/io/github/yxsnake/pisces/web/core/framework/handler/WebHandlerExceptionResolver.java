package io.github.yxsnake.pisces.web.core.framework.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.JsonParseException;
import io.github.yxsnake.pisces.web.core.exception.BizException;
import io.github.yxsnake.pisces.web.core.base.ErrorCode;
import io.github.yxsnake.pisces.web.core.base.Result;
import io.github.yxsnake.pisces.web.core.base.ResultCode;
import io.github.yxsnake.pisces.web.core.utils.ResponseUtils;
import io.github.yxsnake.pisces.web.core.configuration.properties.WebCoreProperties;
import com.google.common.base.Throwables;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import java.util.List;
import java.util.Set;

/**
 * 全程异常处理 {@link org.springframework.web.servlet.HandlerExceptionResolver {@link
 * org.springframework.web.servlet.DispatcherServlet}.
 *
 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
 * @see #handleNoSuchRequestHandlingMethod
 * @see #handleHttpRequestMethodNotSupported
 * @see #handleHttpMediaTypeNotSupported
 * @see #handleMissingServletRequestParameter
 * @see #handleServletRequestBindingException
 * @see #handleTypeMismatch
 * @see #handleHttpMessageNotReadable
 * @see #handleHttpMessageNotWritable
 * @see #handleMethodArgumentNotValidException
 * @see #handleMissingServletRequestParameter
 * @see #handleMissingServletRequestPartException
 * @see #handleBindException
 * @see org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

  private static final ModelAndView MODEL_VIEW_INSTANCE = new ModelAndView();
  private final WebCoreProperties webConf;

  public WebHandlerExceptionResolver(WebCoreProperties webConf) {
    this.webConf = webConf;
  }

  @Override
  protected ModelAndView doResolveException(@NonNull HttpServletRequest request,
                                            @NonNull HttpServletResponse response,
                                            Object handler,
                                            @NonNull Exception ex) {
    try {
      if (ex instanceof BizException) {
        handleBiz((BizException) ex, request, response);
      } else if (ex instanceof HttpRequestMethodNotSupportedException) {
        handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, request,
          response);
      } else if (ex instanceof HttpMediaTypeNotSupportedException) {
        handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, request, response);
      } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
        handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, request,
          response);
      } else if (ex instanceof MissingPathVariableException) {
        handleMissingPathVariable((MissingPathVariableException) ex, request, response);
      } else if (ex instanceof MissingServletRequestParameterException) {
        handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, request, response);
      } else if (ex instanceof ServletRequestBindingException) {
        handleServletRequestBindingException((ServletRequestBindingException) ex, request, response);
      } else if (ex instanceof ConversionNotSupportedException) {
        handleConversionNotSupported((ConversionNotSupportedException) ex, request, response);
      } else if (ex instanceof TypeMismatchException) {
        handleTypeMismatch((TypeMismatchException) ex, request, response);
      } else if (ex instanceof HttpMessageNotReadableException) {
        handleHttpMessageNotReadable((HttpMessageNotReadableException) ex, request, response);
      } else if (ex instanceof HttpMessageNotWritableException) {
        handleHttpMessageNotWritable((HttpMessageNotWritableException) ex, request, response);
      } else if (ex instanceof MethodArgumentNotValidException) {
        handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, request, response);
      } else if (ex instanceof MissingServletRequestPartException) {
        handleMissingServletRequestPartException((MissingServletRequestPartException) ex, request, response);
      } else if (ex instanceof BindException) {
        handleBindException((BindException) ex, request, response);
      } else if (ex instanceof NoHandlerFoundException) {
        handleNoHandlerFoundException((NoHandlerFoundException) ex, request, response);
      } else if (ex instanceof AsyncRequestTimeoutException) {
        handleAsyncRequestTimeoutException((AsyncRequestTimeoutException) ex, request, response);
      } else if (ex instanceof ConstraintViolationException) {
        handleConstraintViolationException((ConstraintViolationException) ex, request, response);
      } else if (ex instanceof NotLoginException){
        handleNotLoginException((NotLoginException) ex,request,response);
      } else if (ex instanceof NotPermissionException){
        handlerNotPermissionException((NotPermissionException)ex,request,response);
      } else if (ex instanceof SaTokenException){
        handlerSaTokenException((SaTokenException) ex,request,response);
      }
      else {
        handleException(ex, request, response);
      }
      if (log.isWarnEnabled()) {
        log.warn("Handling of [" + ex.getClass().getName() + "]  Exception", ex);
      }
    } catch (Exception handlerException) {
      if (log.isWarnEnabled()) {
        log.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception",
          handlerException);
      }
    }
    log.warn("Warn: doResolveException {}", Throwables.getStackTraceAsString(ex));
    return MODEL_VIEW_INSTANCE;
  }

  private void handlerSaTokenException(SaTokenException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.SA_TOKEN_ERROR);
  }

  private void handlerNotPermissionException(NotPermissionException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.NOT_PERMISSION);
  }

  private void handleNotLoginException(NotLoginException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.NOT_LOGIN);
  }

  /**
   * Handle the case where exception
   *
   * @param ex       the BizException to be handled
   * @param request  current HTTP request
   * @param response current HTTP response
   */
  private void handleBiz(BizException ex, HttpServletRequest request, HttpServletResponse response) {
    ErrorCode errorCode = ex.getErrorCode();
    ResponseUtils.writeValueAsJson(response, Result.builder().code(errorCode.getCode()).msg(errorCode.getMsg()).build());
  }

  private void handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response, ResultCode.METHOD_NOT_ALLOWED);
  }

  private void handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.UNSUPPORTED_MEDIA_TYPE);
  }

  private void handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.NOT_ACCEPTABLE);
  }

  private void handleMissingPathVariable(MissingPathVariableException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.MISSING_PATH_VARIABLE);
  }

  private void handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.BAD_REQUEST);
  }

  private void handleServletRequestBindingException(ServletRequestBindingException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.REQUEST_BINDING_ERROR);
  }

  private void handleConversionNotSupported(ConversionNotSupportedException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.INTERNAL_SERVER_ERROR);
  }

  private void handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.TYPE_MISMATCH_ERROR);
  }

  private void handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request, HttpServletResponse response) {
    if (ex.getCause() instanceof JsonParseException) {
      sendFail(response,ResultCode.JSON_FORMAT_ERROR);
    } else {
      sendFail(response,ResultCode.BAD_REQUEST);
    }
  }

  private void handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.NOT_WRITABLE_ERROR);
  }

  private void handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
    List<ObjectError> allErrors = ex.getAllErrors();
    if(CollUtil.isNotEmpty(allErrors)){
      ObjectError objectError = allErrors.get(0);
      String defaultMessage = objectError.getDefaultMessage();
      ResponseUtils.writeValueAsJson(response,Result.builder().code(ResultCode.BAD_REQUEST.getCode()).msg(defaultMessage).build());
    }else{
      sendFail(response,ResultCode.BAD_REQUEST);
    }
  }

  private void handleMissingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.BAD_REQUEST);
  }

  private void handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.BAD_REQUEST);  }

  private void handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.NOT_FOUND);
  }

  private void handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.SERVICE_UNAVAILABLE);
  }

  private void handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request, HttpServletResponse response) {
    Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
    String message = constraintViolations.stream().findFirst().get().getMessage();
    ResponseUtils.writeValueAsJson(response,Result.builder().code(HttpServletResponse.SC_BAD_REQUEST).msg(message).build());
  }

  private void handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
    sendFail(response,ResultCode.INTERNAL_SERVER_ERROR);
  }

  private void sendFail(HttpServletResponse response,ResultCode resultCode){
    ResponseUtils.writeValueAsJson(response,Result.builder().code(resultCode.getCode()).msg(resultCode.getMsg()).build());
  }


}

