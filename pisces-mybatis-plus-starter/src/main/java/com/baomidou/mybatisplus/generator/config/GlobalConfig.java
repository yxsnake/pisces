package com.baomidou.mybatisplus.generator.config;

/**
 * @author: snake
 * @create-time: 2024-09-04
 * @description:
 * @version: 1.0
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 全局配置
 *
 * @author hubin
 * @since 2016-12-02
 */
@Data
@Accessors(chain = true)
public class GlobalConfig {

    /**
     * 生成文件的输出目录【默认 D 盘根目录】
     */
    private String outputDir = "D://";
    /**
     * 生成文件的输出根目录
     */
    private String rootOutputDir = "";

    /**
     * 是否覆盖已有文件
     */
    private boolean fileOverride = false;

    /**
     * 是否打开输出目录
     */
    private boolean open = true;

    /**
     * 是否在xml中添加二级缓存配置
     */
    private boolean enableCache = false;

    /**
     * 开发人员
     */
    private String author;

    /**
     * 开启 Kotlin 模式
     */
    private boolean kotlin = false;

    /**
     * 开启 swagger3 模式
     */
    private boolean swagger3 = false;

    /**
     * 开启 ActiveRecord 模式
     */
    private boolean activeRecord = false;

    /**
     * 开启 BaseResultMap
     */
    private boolean baseResultMap = false;

    /**
     * 时间类型对应策略
     */
    private DateType dateType = DateType.TIME_PACK;

    /**
     * 开启 baseColumnList
     */
    private boolean baseColumnList = false;
    /**
     * 各层文件名称方式，例如： %sAction 生成 UserAction
     * %s 为占位符
     */
    private String entityName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    private String httpClientName;
    private String metaObjectHandlerName;
    /**
     * 指定生成的主键的ID类型
     */
    private IdType idType;
}
