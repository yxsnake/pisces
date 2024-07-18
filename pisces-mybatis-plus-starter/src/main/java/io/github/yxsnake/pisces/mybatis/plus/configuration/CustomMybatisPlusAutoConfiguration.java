package io.github.yxsnake.pisces.mybatis.plus.configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.*;
import io.github.yxsnake.pisces.mybatis.plus.configuration.properties.TenantProperties;
import io.github.yxsnake.pisces.mybatis.plus.handler.CustomTenantHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(TenantProperties.class)
@Configuration
@RequiredArgsConstructor
public class CustomMybatisPlusAutoConfiguration {

  private final TenantProperties tenantProperties;

  private final CustomTenantHandler customTenantHandler;

  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

    // 添加非法SQL拦截器
    interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());

    if(tenantProperties.getEnabled()){
      TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
      tenantInterceptor.setTenantLineHandler(customTenantHandler);
      interceptor.addInnerInterceptor(tenantInterceptor);
    }
    // 配置分页插件
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
    // 防全表更新删除操作
    interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
    // 增加@Version乐观锁支持
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    return interceptor;
  }


}
