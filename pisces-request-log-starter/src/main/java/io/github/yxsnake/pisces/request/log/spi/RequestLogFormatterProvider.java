package io.github.yxsnake.pisces.request.log.spi;

import io.github.yxsnake.pisces.request.log.bean.ExpandMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;

/**
 * @author snake
 */

public interface RequestLogFormatterProvider extends Ordered {

  /**
   * 自定义请求日志输出格式
   *
   * @param request 请求信息
   * @param expandMessage 扩展信息
   * @return 日志信息
   */
  String getRequestLogMessage(HttpServletRequest request, ExpandMessage expandMessage);

  @Override
  int getOrder();
}
