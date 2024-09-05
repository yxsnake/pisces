package com.baomidou.mybatisplus.generator.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.github.yxsnake.pisces.web.core.constant.StringPool;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author: snake
 * @create-time: 2024-09-04
 * @description: 跟包相关的配置项
 * @version: 1.0
 */
@Data
@Accessors(chain = true)
public class PackageConfig {

    /**
     * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
     */
    private String parent = "com.snake";
    /**
     * 父包模块名
     */
    private String moduleName = "";
    /**
     * Entity包名
     */
    private String entity = "entity";
    /**
     * Service包名
     */
    private String service = "service";
    /**
     * Service Impl包名
     */
    private String serviceImpl = "service.impl";
    /**
     * Mapper包名
     */
    private String mapper = "mapper";
    /**
     * Mapper XML包名
     */
    private String xml = "mapper.xml";
    /**
     * Controller包名
     */
    private String controller = "controller";
    /**
     * Http request client包名
     */
    private String httpClient = "";
    /**
     * meta object handler包名
     */
    private String metaObjectHandler = "handler.mybatis";
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;

    /**
     * 父包名
     */
    public String getParent() {
        if (StringUtils.isNotBlank(moduleName)) {
            return parent + StringPool.DOT + moduleName;
        }
        return parent;
    }
}
