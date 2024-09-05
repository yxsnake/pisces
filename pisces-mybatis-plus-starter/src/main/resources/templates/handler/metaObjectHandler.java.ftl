package ${cfg.metaObjectPackage};

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

/**
* 通用字段自动填充
*
* @author snake
**/
@Component
public class KyMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    ${insertMetaObjectHandlerStr}
  }


  @Override
  public void updateFill(MetaObject metaObject) {
    ${updateMetaObjectHandlerStr}
  }


}
