package io.github.yxsnake.pisces.web.core.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.Objects;


@Data
@Schema(description = "查询请求参数")
public class QueryFilter<T,R> {

  @Schema(description = "当前页码(默认第1页)",requiredMode = Schema.RequiredMode.NOT_REQUIRED,format = "int64",example = "1")
  private Long pageNum = 1L;

  @Schema(description = "页容量(默认10条数据)",requiredMode = Schema.RequiredMode.NOT_REQUIRED,format = "int64",example = "1")
  private Long pageSize = 10L;

  @Schema(description = "排序字段",requiredMode = Schema.RequiredMode.NOT_REQUIRED,format = "string",example = "createTime")
  private String sortField;

  @Schema(description = "排序方式(为空时默认为升序，0：升序，1:降序)",requiredMode = Schema.RequiredMode.NOT_REQUIRED,format = "int32",example = "1")
  private Integer sortType = 1;

  @Schema(description = "等值查询(直接性查询条件对象(T是一个 Bean 对象，可以根据对象的属性作为=条件查询)",requiredMode = Schema.RequiredMode.NOT_REQUIRED,format = "object",example = "")
  private T equalsQueries;

  @Schema(description = "模糊查询条件对象(R只是一个Bean,可以根据对象的属性作为模糊查询条件)",requiredMode = Schema.RequiredMode.NOT_REQUIRED,format = "object",example = "")
  private R fuzzyQueries;

  public QueryFilter(){
    this.pageNum = 1L;
    this.pageSize = 10L;
    this.sortType = 1;
    this.sortField = "createTime";
  }

  public static <T> T getEqualsQueries(Class<T> clazz,T t) {
    if(Objects.isNull(t)){
      try {
        Constructor<T> constructor = clazz.getConstructor();
        return constructor.newInstance();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
    return t;
  }
  public static <R> R getFuzzyQueries(Class<R> clazz,R r) {
    if(Objects.isNull(r)){
      try {
        Constructor<R> constructor = clazz.getConstructor();
        return constructor.newInstance();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
    return r;
  }
}
