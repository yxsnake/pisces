package io.github.yxsnake.pisces.web.core.enums;

import io.github.yxsnake.pisces.web.core.base.IBaseEnum;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author: snake
 * @create-time: 2024-06-25
 * @description: 是否删除 枚举
 * @version: 1.0
 */
@Getter
public enum DeletedEnum implements IBaseEnum<Integer> {

    DEL(1,"已删除"),

    NORMAL(0,"正常"),

    ;

    private final Integer value;

    private final String label;

    DeletedEnum(final Integer value,final String label){
        this.value = value;
        this.label = label;
    }

    public static DeletedEnum getInstance(final Integer value){
        return Arrays.asList(values()).stream().filter(item->item.getValue().equals(value)).findFirst().orElse(null);
    }
}
