package io.github.yxsnake.pisces.mybatis.plus.handler;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import io.github.yxsnake.pisces.mybatis.plus.configuration.properties.TenantProperties;
import io.github.yxsnake.pisces.mybatis.plus.context.TenantIgnoreContext;
import lombok.AllArgsConstructor;
import io.github.yxsnake.pisces.web.core.context.UserContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import java.util.List;

@AllArgsConstructor
public class CustomTenantHandler implements TenantLineHandler {

  private final TenantProperties tenantProperties;

  @Override
  public Expression getTenantId() {
    // 假设有一个租户上下文，能够从中获取当前用户的租户
    String tenantId = UserContext.getTenantId();
    // 返回租户ID的表达式，StringValue 是 JSQLParser 中表示 string 类型的 class
    return new StringValue(tenantId);
  }

  @Override
  public String getTenantIdColumn() {
    return "tenant_id";
  }

  @Override
  public boolean ignoreTable(String tableName) {
    boolean b = TenantIgnoreContext.get();
    if(b){
      TenantIgnoreContext.remove();
      return true;
    }else{
      List<String> ignoreTables = tenantProperties.getIgnoreTables();
      if(CollUtil.isNotEmpty(ignoreTables)){
        return ignoreTables.contains(tableName);
      }
      // 根据需要返回是否忽略该表
      return false;
    }
  }
}
