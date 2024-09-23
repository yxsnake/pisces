package io.github.yxsnake.pisces.excel.core;

import java.util.List;

/**
 * @author snake
 * @description excel返回对象
 * @since 2024/9/23 23:30
 */

public interface ExcelResult<T> {

    /**
     * 对象列表
     */
    List<T> getList();

    /**
     * 错误列表
     */
    List<String> getErrorList();

    /**
     * 导入回执
     */
    String getAnalysis();
}

