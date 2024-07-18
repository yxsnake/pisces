package io.github.yxsnake.pisces.mybatis.plus.configuration;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class CustomP6spyConfiguration implements MessageFormattingStrategy {

  @Override
  public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
    Map<String, Object> message = new LinkedHashMap<>(8);
    String newPrepared = prepared.replace("   ", "").replace("\n", " ");
    message.put("prepared", newPrepared);
    String newSql = sql.replace("   ", "").replace("\n", " ");
    message.put("sql", newSql);
    return JsonUtils.objectCovertToJson(message);
  }

}
