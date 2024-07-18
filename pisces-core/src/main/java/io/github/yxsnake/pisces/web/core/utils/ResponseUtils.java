package io.github.yxsnake.pisces.web.core.utils;

import com.google.common.base.Throwables;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author snake
 * @description
 * @since 2024/1/15 23:15
 */
@Slf4j
public class ResponseUtils {

  /**
   * @param response 响应对象
   * @param object   响应数据
   */
  public static void writeValueAsJson(HttpServletResponse response, Object object) {
    writeValueAsJson(response, "application/json", object);
  }

  /**
   * 设置webflux模型响应
   *
   * @param response    ServerHttpResponse
   * @param contentType content-type
   * @param status      http状态码
   * @param value       响应内容
   * @return Mono<Void>
   */
  public static Mono<Void> makeWebFluxResponse(ServerHttpResponse response, String contentType,
                                               HttpStatus status, Object value) {
    response.setStatusCode(status);
    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
    DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtils.objectCovertToJson(value).getBytes());
    return response.writeWith(Mono.just(dataBuffer));
  }

  public static Mono<Void> makeWebFluxResponse(ServerHttpResponse response, Object value) {
    response.setStatusCode(HttpStatus.OK);
    response.getHeaders().add("Content-Type", "application/json; charset=utf-8");
    DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtils.objectCovertToJson(value).getBytes());
    return response.writeWith(Mono.just(dataBuffer));
  }

  /**
   * 数据写出浏览器
   *
   * @param response
   * @param object
   */
  public static void writeValueAsJson(HttpServletResponse response, String contentType, Object object) {
    response.setContentType(contentType);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    try {
      PrintWriter writer = response.getWriter();
      try {
        writer.print(JsonUtils.objectCovertToJson(object));
        writer.flush();
      } catch (Throwable e1) {
        if (writer != null) {
          try {
            writer.close();
          } catch (Throwable e2) {
            e1.addSuppressed(e2);
          }
        }
        throw e1;
      }
      if (writer != null) {
        writer.close();
      }
    } catch (IOException e) {
      log.warn("Error: Response printJson failed, stackTrace: {}", Throwables.getStackTraceAsString(e));
    }
  }
}


