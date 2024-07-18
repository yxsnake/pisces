package io.github.yxsnake.pisces.apollo.spi;

import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.core.spi.MetaServerProvider;
import io.github.yxsnake.pisces.web.core.constant.StringPool;

import java.util.HashMap;
import java.util.Map;

public class SnakeMetaServerProvider implements MetaServerProvider {


  public static final int ORDER = -10;
  private static final Map<Env, String> DOMAINS = new HashMap<>();

  private static final String HTTP_PROTOCOL_PREFIX = "http"+ StringPool.COLON+StringPool.SLASH+StringPool.SLASH;

  public SnakeMetaServerProvider() {
    initialize();
  }

  private void initialize() {
    DOMAINS.put(Env.LOCAL, HTTP_PROTOCOL_PREFIX+"apollo-dev-meta.pisces.cn");
    DOMAINS.put(Env.DEV,   HTTP_PROTOCOL_PREFIX+"apollo-dev-meta.pisces.cn");
    DOMAINS.put(Env.TEST,  HTTP_PROTOCOL_PREFIX+"apollo-test-meta.pisces.cn");
    DOMAINS.put(Env.PRO,   HTTP_PROTOCOL_PREFIX+"apollo-pro-meta.pisces.cn");
  }


  @Override
  public String getMetaServerAddress(Env env) {
    return null;
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
