package io.github.yxsnake.pisces.elasticsearch.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import io.github.yxsnake.pisces.web.core.utils.JsonUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author snake
 * @description ElasticSearch HighLevelClient
 * @since 2023/8/19 23:39
 */
@Slf4j
//@ConfigurationProperties(prefix = "spring.elasticsearch.rest")
//@Configuration
public class RestHighLevelClientAutoConfiguration {

  @Setter
  private List<String> clusterNodes;

  @Setter
  private Boolean auth = Boolean.FALSE;

  @Setter
  private String username;

  @Setter
  private String password;

//  @PostConstruct
  public void init() {
    log.info("------------ elasticsearch-starter StartUp Information -----------");
    log.info("elasticsearch-starter");
    log.info("    |-clusterNodes: {}", JsonUtils.objectCovertToJson(clusterNodes));
    log.info("    |-username: {}", username);
    log.info("    |-password: {}", password);
    log.info("-------------------------------------------------------------");
  }


//  @Bean
  public RestHighLevelClient restHighLevelClient() {
    HttpHost[] hosts = clusterNodes.stream()
      // eg: new HttpHost("127.0.0.1", 9200, "http")
      .map(this::buildHttpHost)
      .toArray(HttpHost[]::new);

    RestClientBuilder builder = RestClient.builder(hosts);
    if(auth){
      CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
      credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
      builder.setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider));
    }
    return new RestHighLevelClient(builder);
  }

  private HttpHost buildHttpHost(String node) {
    String[] nodeInfo = node.split(":");
    return new HttpHost(nodeInfo[0].trim(), Integer.parseInt(nodeInfo[1].trim()), "http");
  }
}
