package io.github.yxsnake.pisces.xxl.job.configuration;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import io.github.yxsnake.pisces.xxl.job.configuration.properties.XxlJobProperties;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author snake
 * @description
 * @since 2024/1/16 22:42
 */
@Slf4j
@Configuration
@Import({XxlJobProperties.class})
@ConditionalOnProperty(name = "xxl.job.enabled", havingValue = "true", matchIfMissing = true)
@AllArgsConstructor
public class XxlJobAutoConfiguration {
  public static final String LOG_PATH_FORMAT = "/data/java_log/%s/xxl-job/jobhandler";
  public static final int LOG_RETENTION_DAYS = 7;

  private final XxlJobProperties xxlJobProperties;

  @PostConstruct
  public void init() {
    log.info("------------ xxl-job-starter StartUp Information -----------");
    log.info("xxl-job");
    log.info("  |-enabled: {}", xxlJobProperties.getEnabled());
    log.info("  |-address: {}", xxlJobProperties.getAddress());
    log.info("  |-token:   {}", xxlJobProperties.getToken());
    log.info("  |-ip: {}", xxlJobProperties.getIp());
    log.info("-------------------------------------------------------------");
  }

  @Bean
  public XxlJobSpringExecutor xxlJobExecutor() {
    XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();


    xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAddress());
    xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getToken());
    xxlJobSpringExecutor.setAppname(xxlJobProperties.getApplicationName());
    xxlJobSpringExecutor.setAddress(xxlJobProperties.getAddress());
    xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
    xxlJobSpringExecutor.setLogPath(String.format(LOG_PATH_FORMAT, xxlJobProperties.getApplicationName()));
    xxlJobSpringExecutor.setLogRetentionDays(LOG_RETENTION_DAYS);

    return xxlJobSpringExecutor;
  }
}


