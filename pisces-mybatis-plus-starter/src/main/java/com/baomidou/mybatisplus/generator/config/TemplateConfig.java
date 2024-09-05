package com.baomidou.mybatisplus.generator.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author: snake
 * @create-time: 2024-09-04
 * @description: 模板路径配置项
 * @version: 1.0
 */
@Data
@Accessors(chain = true)
public class TemplateConfig {

    @Getter(AccessLevel.NONE)
    private String entity = ConstVal.TEMPLATE_ENTITY_JAVA;

    private String entityKt = ConstVal.TEMPLATE_ENTITY_KT;

    private String service = ConstVal.TEMPLATE_SERVICE;

    private String serviceImpl = ConstVal.TEMPLATE_SERVICE_IMPL;

    private String mapper = ConstVal.TEMPLATE_MAPPER;

    private String xml = ConstVal.TEMPLATE_XML;

    private String controller = ConstVal.TEMPLATE_CONTROLLER;

    private String httpClient = ConstVal.TEMPLATE_HTTP_CLIENT;

    private String metaObjectHandler = ConstVal.TEMPLATE_META_OBJECT_HANDLER;

    public String getEntity(boolean kotlin) {
        return kotlin ? entityKt : entity;
    }

    /**
     * 禁用模板
     *
     * @param templateTypes 模板类型
     * @return this
     * @since 3.3.2
     */
    public TemplateConfig disable(TemplateType... templateTypes) {
        if (templateTypes != null && templateTypes.length > 0) {
            for (TemplateType templateType : templateTypes) {
                switch (templateType) {
                    case XML:
                        setXml(null);
                        break;
                    case ENTITY:
                        setEntity(null).setEntityKt(null);
                        break;
                    case MAPPER:
                        setMapper(null);
                        break;
                    case SERVICE:
                        setService(null).setServiceImpl(null);
                        break;
                    case CONTROLLER:
                        setController(null);
                        break;
                    case HTTP_CLIENT:
                        setHttpClient(null);
                        break;
                    case META_OBJECT_HANDLER:
                        setMetaObjectHandler(null);
                        break;
                }
            }
        }
        return this;
    }

}

