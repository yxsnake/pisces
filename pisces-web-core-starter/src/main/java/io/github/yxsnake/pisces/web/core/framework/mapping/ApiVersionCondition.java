package io.github.yxsnake.pisces.web.core.framework.mapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本条件
 * @version v1.0
 **/
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {


  /**
   * 路径中版本的前缀， 这里用 /v[1-9]/的形式
   */
  private static final Pattern VERSION_PREFIX_PATTERN = Pattern.compile("/v([0-9]+\\.{0,1}[0-9]{0,2})/");

  private ApiVersion apiVersion;

  public ApiVersionCondition(ApiVersion apiVersion){
    this.apiVersion = apiVersion;
  }
  /**
   * [当class 和 method 请求url相同时，触发此方法用于合并url]
   * 官方解释：
   * - 某个接口有多个规则时，进行合并
   * - 比如类上指定了@RequestMapping的 url 为 root
   * - 而方法上指定的@RequestMapping的 url 为 method
   * - 那么在获取这个接口的 url 匹配规则时，类上扫描一次，方法上扫描一次，这个时候就需要把这两个合并成一个，表示这个接口匹配root/method
   * @param other 相同api version condition
   * @return ApiVersionCondition
   */
  @Override
  public ApiVersionCondition combine(@NonNull ApiVersionCondition other) {
    return new ApiVersionCondition(other.getApiVersion());
  }

  @Override
  public ApiVersionCondition getMatchingCondition(@NonNull HttpServletRequest request) {
    Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
    if (m.find()) {
      // 获得符合匹配条件的ApiVersionCondition
      double version = Double.valueOf(m.group(1));
      if (version >= getApiVersion().version()) {
        return this;
      }
    }
    return null;
  }
  /**
   * 多个都满足条件时，用来指定具体选择哪一个
   * @param other 多个时
   * @param request http request
   * @return 取版本号最大的
   */
  @Override
  public int compareTo(@NonNull ApiVersionCondition other, @NonNull HttpServletRequest request) {
    return other.getApiVersion().version() >= getApiVersion().version() ? 1 : -1;
  }

  public ApiVersion getApiVersion() {
    return apiVersion;
  }

}

