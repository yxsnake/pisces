package io.github.yxsnake.pisces.web.core.framework.controller.health;

import cn.hutool.core.date.DateUtil;
import io.github.yxsnake.pisces.web.core.framework.model.HealthVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.github.yxsnake.pisces.web.core.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author snake
 * @description
 * @since 2024/1/16 21:50
 */
@Tag(name = "服务健康检查")
@Slf4j
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@ConditionalOnProperty(prefix = "web-core.health", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HealthController {

  @Value("${spring.application.name}")
  private String applicationName;

  public HealthController(){
    log.info("健康检查接口已加载.....................");
  }

  @Operation(summary = "健康检查接口")
  @GetMapping("${web-core.health.path:/health}")
  public Result<HealthVO> health() {
    return Result.success(HealthVO.builder().applicationName(applicationName).currentDate(DateUtil.now()).build());
  }
}

