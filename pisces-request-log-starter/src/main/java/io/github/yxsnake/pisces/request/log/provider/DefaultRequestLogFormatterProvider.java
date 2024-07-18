package io.github.yxsnake.pisces.request.log.provider;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import io.github.yxsnake.pisces.web.core.utils.IpUtil;
import io.github.yxsnake.pisces.web.core.utils.RequestUtils;
import io.github.yxsnake.pisces.request.log.bean.ExpandMessage;
import io.github.yxsnake.pisces.request.log.spi.RequestLogFormatterProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.UrlPathHelper;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * 默认提供的请求日志输出格式实现
 * 格式：
 * <-- GET /abc/def methodName {xx:xx, xx:xx, ...}
 *
 * @author maye
 */
public class DefaultRequestLogFormatterProvider implements RequestLogFormatterProvider {

  // make it as lowest as possible, yet not the lowest
  public static final int ORDER = RequestLogFormatterProvider.HIGHEST_PRECEDENCE - 1;
  private static final UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();

  @Override
  public String getRequestLogMessage(HttpServletRequest request, ExpandMessage expandMessage) {
    return StrUtil.format("<-- {} {} {}",
            "请求类型:"+request.getMethod(),
            "请求接口URI:"+URL_PATH_HELPER.getOriginatingRequestUri(request),
            getRequestParamsStr(request));
  }

  private String getRequestParamsStr(HttpServletRequest request){
    long receivedTimeMilli = System.currentTimeMillis();
    JSONObject params = new JSONObject(new LinkedHashMap<>());
    // 请求附带的参数
    params.put("params参数", request.getParameterMap());
    // 请求体，如果为空则不打印
    params.put("body参数", parseBody(RequestUtils.getRequestBody(request)));
    // 请求到达时间
    params.put("请求到达时间", LocalDateTimeUtil.of(receivedTimeMilli));
    params.put("请求来源ID", IpUtil.getIpAddr(request));
    return params.toString();
  }

  private Object parseBody(String requestBody) {
    try {
      return Optional.ofNullable(JSONObject.parse(requestBody)).orElse(new JSONObject());
    } catch (Exception e) {
      return requestBody;
    }
  }

  @Override
  public int getOrder() {
    return ORDER;
  }

}
