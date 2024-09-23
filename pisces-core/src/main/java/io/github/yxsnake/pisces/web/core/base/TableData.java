package io.github.yxsnake.pisces.web.core.base;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author snake
 * @description 表格分页数据对象
 * @since 2024/9/24 7:51
 */
@Data
@NoArgsConstructor
public class TableData<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableData(List<T> list, long total) {
        this.rows = list;
        this.total = total;
    }

    /**
     * 根据分页对象构建表格分页数据对象
     */
    public static <T> TableData<T> build(IPage<T> page) {
        TableData<T> rspData = new TableData<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.setRows(page.getRecords());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    /**
     * 根据数据列表构建表格分页数据对象
     */
    public static <T> TableData<T> build(List<T> list) {
        TableData<T> rspData = new TableData<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 构建表格分页数据对象
     */
    public static <T> TableData<T> build() {
        TableData<T> rspData = new TableData<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        return rspData;
    }

}
