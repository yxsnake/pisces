package io.github.yxsnake.pisces.mybatis.plus.handler;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.github.yxsnake.pisces.web.core.context.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    log.info("start insert fill ....");
    this.strictInsertFill(metaObject, "createTime", Date.class, DateUtil.date()); // 起始版本 3.3.0(推荐使用)
    this.strictInsertFill(metaObject, "createUserId", String.class, UserContext.getUserId());
    this.strictInsertFill(metaObject,"createUserName",String.class,UserContext.getRealName());
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    log.info("start update fill ....");
    this.strictUpdateFill(metaObject, "updateTime", Date.class, DateUtil.date()); // 起始版本 3.3.0(推荐)
    this.strictUpdateFill(metaObject, "updateUserId", String.class, UserContext.getUserId());
    this.strictUpdateFill(metaObject,"updateUserName",String.class,UserContext.getRealName());
  }
}
