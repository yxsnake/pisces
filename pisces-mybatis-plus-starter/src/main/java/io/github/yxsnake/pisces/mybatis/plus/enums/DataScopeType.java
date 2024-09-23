package io.github.yxsnake.pisces.mybatis.plus.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author snake
 * @description
 * @since 2024/9/24 0:53
 */

@Getter
@AllArgsConstructor
public enum DataScopeType {

    /**
     * 全部数据权限
     */
    ALL("1", "", ""),

    /**
     * 自定数据权限
     */
    CUSTOM("2", " #{#deptName} IN ( #{@sdss.getRoleCustom( #user.roleId )} ) ", " 1 = 0 "),

    /**
     * 部门数据权限
     */
    DEPT("3", " #{#deptName} = #{#user.deptId} ", " 1 = 0 "),

    /**
     * 部门及以下数据权限
     */
    DEPT_AND_CHILD("4", " #{#deptName} IN ( #{@sdss.getDeptAndChild( #user.deptId )} )", " 1 = 0 "),

    /**
     * 仅本人数据权限
     */
    SELF("5", " #{#userName} = #{#user.userId} ", " 1 = 0 ");

    private final String code;

    /**
     * SpEL 模板表达式，用于构建 SQL 条件
     */
    private final String sqlTemplate;

    /**
     * 如果不满足 {@code sqlTemplate} 的条件，则使用此默认 SQL 表达式
     */
    private final String elseSql;

    /**
     * 根据枚举代码查找对应的枚举值
     *
     * @param code 枚举代码
     * @return 对应的枚举值，如果未找到则返回 null
     */
    public static DataScopeType findCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (DataScopeType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}

