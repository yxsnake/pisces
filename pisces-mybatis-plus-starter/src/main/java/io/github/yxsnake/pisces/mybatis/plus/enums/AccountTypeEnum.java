package io.github.yxsnake.pisces.mybatis.plus.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author snake
 * @description 账号类型
 * @since 2024/9/24 1:08
 */
@Getter
public enum AccountTypeEnum {
    /**
     * 员工pc
     */
    EMP_PC("emp_pc"),
    /**
     * 员工app
     */
    EMP_APP("emp_app"),
    /**
     * 会员app
     */
    MEMBER_APP("member_app"),
    /**
     * 会员h5
     */
    MEMBER_H5("member_h5"),
    /**
     * 会员微信小程序
     */
    MEMBER_WX_SMALL_PROGRAM("member_small_program"),

    /**
     * 商户PC
     */
    MERCHANT_PC("merchant_pc")

    ;
    private final String accountType;

    AccountTypeEnum(final String accountType){
        this.accountType = accountType;
    }

    public static AccountTypeEnum getInstance(final String accountType){
        return Arrays.asList(values()).stream().filter(item->item.getAccountType().equals(accountType)).findFirst().orElse(null);
    }

    public static AccountTypeEnum getAccountType(String loginType) {
        for (AccountTypeEnum value : values()) {
            if (StringUtils.contains(loginType, value.getAccountType())) {
                return value;
            }
        }
        throw new RuntimeException("'UserType' not found By " + loginType);
    }
}
