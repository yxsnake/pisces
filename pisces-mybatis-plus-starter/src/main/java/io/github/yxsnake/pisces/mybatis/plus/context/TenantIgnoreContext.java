package io.github.yxsnake.pisces.mybatis.plus.context;


import java.util.Objects;

/**
 * @author: snake
 * @create-time: 2024-06-27
 * @description: 忽略租户内数据查询
 * @version: 1.0
 */
public class TenantIgnoreContext {

    private final static ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    private TenantIgnoreContext() {
    }

    public static void set() {
        CONTEXT.set("ignore");
    }

    public static boolean get() {
        return Objects.isNull(CONTEXT.get())?Boolean.FALSE:Boolean.TRUE;
    }

    public static void remove() {
        CONTEXT.remove();
    }
}
