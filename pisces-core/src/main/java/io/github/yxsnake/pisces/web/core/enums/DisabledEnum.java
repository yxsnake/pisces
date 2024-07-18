package io.github.yxsnake.pisces.web.core.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-06-25
 * @description: 账号禁用状态 枚举
 * @version: 1.0
 */
@Getter
public enum DisabledEnum implements IBaseEnum<Integer> {

    NORMAL(0,"正常"),

    DISABLE(1,"禁用"),

    ;

    private final Integer value;

    private final String label;

    DisabledEnum(final Integer value,final String label){
        this.value = value;
        this.label = label;
    }

    public static DisabledEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}
