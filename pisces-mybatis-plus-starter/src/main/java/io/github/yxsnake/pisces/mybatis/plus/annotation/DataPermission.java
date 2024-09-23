package io.github.yxsnake.pisces.mybatis.plus.annotation;

import java.lang.annotation.*;

/**
 * @author snake
 * @description 数据权限组注解，用于标记数据权限配置数组
 * @since 2024/9/24 0:52
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    /**
     * 数据权限配置数组，用于指定数据权限的占位符关键字和替换值
     *
     * @return 数据权限配置数组
     */
    DataColumn[] value();

    /**
     * 权限拼接标识符(用于指定连接语句的sql符号)
     * 如不填 默认 select 用 OR 其他语句用 AND
     * 内容 OR 或者 AND
     */
    String joinStr() default "";

}

