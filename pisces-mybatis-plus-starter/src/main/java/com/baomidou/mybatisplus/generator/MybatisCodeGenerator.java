package com.baomidou.mybatisplus.generator;

/**
 * @author: snake
 * @create-time: 2024-09-04
 * @description:
 * @version: 1.0
 */
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.cons.PathCons;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import io.github.yxsnake.pisces.web.core.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.*;

import static io.github.yxsnake.pisces.web.core.constant.StringPool.DOT;
import static io.github.yxsnake.pisces.web.core.constant.StringPool.SLASH;


/**
 * 基于Mybatis-plus代码生成器
 *
 * @author snake
 * @version v1.0
 * @date 2023-07-19
 **/
@Setter
@Getter
@Accessors(chain = true)
public class MybatisCodeGenerator {

    private DataSourceConfig dataSourceConfig;

    private GlobalConfig globalConfig;

    private TemplateConfig templateConfig;

    private KyStrategyConfig strategyConfig;

    private KyPackageConfig packageConfig;

    private InjectionConfig injectionConfig;

    public MybatisCodeGenerator() {
        strategyConfig = (KyStrategyConfig) new KyStrategyConfig()
                // 自定义实体父类
//            .setSuperEntityClass(BaseEntity.class)
                // 自定义实体，公共字段
//            .setSuperEntityColumns("id")
                // 【实体】是否为lombok模型（默认 false）
                .setEntityLombokModel(true)
                // Boolean类型字段是否移除is前缀处理
                .setEntityBooleanColumnRemoveIsPrefix(true)
                .setRestControllerStyle(true)
                .setNaming(NamingStrategy.underline_to_camel)
                // 是否生成实体时，生成字段注解
                .setEntityTableFieldAnnotationEnable(true)
        ;
        // 自定义 controller 父类
        strategyConfig.setSuperControllerClass(
                "io.github.yxsnake.pisces.web.core.framework.controller.BaseController");

        dataSourceConfig = new DataSourceConfig();

        packageConfig = (KyPackageConfig) new KyPackageConfig()
                .setDto("model.dto")
                .setBo("model.bo")
                .setHttpClient("http-request/client/")
                .setParent("com.snake")
                .setController("controller")
                .setEntity("model.entity")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl");

        templateConfig = new TemplateConfig()
                .setHttpClient("templates/httpClient.http")
                .setXml(null);

        globalConfig = new GlobalConfig()
                //输出目录
                .setOutputDir(getJavaPath())
                .setRootOutputDir(getRootPath())
                .setBaseResultMap(true)
                .setBaseColumnList(true)
                .setOpen(false)
                .setAuthor("pisces")
                //使用自定义模板覆盖默认模板
                .setFileOverride(true);

        injectionConfig = buildInjectionConfig();
    }

    private InjectionConfig buildInjectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
//        Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);
                Map<String, Object> map = new HashMap<>();
                map.put("dto", getPackageConfig().getParent() + StringPool.DOT + packageConfig.getDto());
                map.put("bo", getPackageConfig().getParent() + StringPool.DOT + packageConfig.getBo());
                map.put("form", getPackageConfig().getParent() + StringPool.DOT + packageConfig.getForm());
                map.put("controllerRestApi", strategyConfig.isControllerRestApi());

                // 自动填充 handler 的输出路径
//                String metaObjectPackageName = getPackageConfig().getParent() + DOT + packageConfig.getMetaObjectHandler();
//                map.put("metaObjectPackage", metaObjectPackageName);

                this.setMap(map);
            }
        }.setFileOutConfigList(this.fileOutConfigList());
    }

    private List<FileOutConfig> fileOutConfigList() {
        List<FileOutConfig> fileOutConfigList = new LinkedList<>();
        fileOutConfigList.add(new FileOutConfig(
                PathCons.MAPPER_TEMPLATES_PATH) {
            // 自定义 mapper 输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                return getResourcePath() + "/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        fileOutConfigList.add(new FileOutConfig(
                PathCons.DTO_TEMPLATES_PATH) {
            // 自定义 DTO 输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                String packageName = (getPackageConfig().getParent() + DOT + getPackageConfig().getDto() + DOT)
                        .replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
                return getJavaPath() + SLASH + packageName + tableInfo.getEntityName() + "DTO.java";
            }
        });
        fileOutConfigList.add(new FileOutConfig(
                PathCons.BO_TEMPLATES_PATH) {
            // 自定义 BO 输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                String packageName = (getPackageConfig().getParent() + DOT + getPackageConfig().getBo() + DOT)
                        .replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
                return getJavaPath() + SLASH + packageName + tableInfo.getEntityName() + "BO.java";
            }
        });
        fileOutConfigList.add(new FileOutConfig(
                PathCons.FORM_TEMPLATES_PATH) {
            // 自定义 Form 输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
                String packageName = (getPackageConfig().getParent() + DOT + getPackageConfig().getBo() + DOT)
                        .replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
                return getJavaPath() + SLASH + packageName + tableInfo.getEntityName() + "Form.java";
            }
        });
        if (strategyConfig.isControllerRestApi()) {
          fileOutConfigList.add(new FileOutConfig(
            PathCons.HTTP_TEMPLATES_PATH) {
            // 自定义 .http 输出文件目录
            @Override
            public String outputFile(TableInfo tableInfo) {
              return "http-request" + SLASH + "client" + SLASH + tableInfo.getEntityName() + ".http";
            }
          });
        }
//        fileOutConfigList.add(new FileOutConfig(
//                PathCons.MEAT_OBJECT_HANDLER_TEMPLATES_PATH) {
//            // 自定义 mybatis plus meta object handler 输出文件目录
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                String packageName = (getPackageConfig().getParent() + DOT + getPackageConfig().getMetaObjectHandler() + DOT)
//                        .replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
//                return getJavaPath() + SLASH + packageName + "KyMetaObjectHandler.java";
//            }
//        });
        return fileOutConfigList;
    }

    /**
     * 获取根目录
     */
    private String getRootPath() {
        String file = Objects.requireNonNull(this.getClass().getClassLoader().getResource(""))
                .getFile();
        return new File(file).getParentFile().getParent();
    }

    /**
     * 获取JAVA目录
     */
    private String getJavaPath() {
        String javaPath = getRootPath() + "/src/main/java";
        System.err.println(" Generator Java Path:【 " + javaPath + " 】");
        return javaPath;
    }

    /**
     * 获取Resource目录
     */
    private String getResourcePath() {
        String resourcePath = getRootPath() + "/src/main/resources";
        System.err.println(" Generator Resource Path:【 " + resourcePath + " 】");
        return resourcePath;
    }

    /**
     * 获取test目录
     */
    private String getTestPath() {
        String testPath = getRootPath() + "/src/test/java";
        System.err.println(" Generator Test Path:【 " + testPath + " 】");
        return testPath;
    }

    /**
     * 获取TableFill策略
     */
//  private List getTableFills() {
//    // 自定义需要填充的字段
//    List tableFillList = new ArrayList<>();
//    tableFillList.add(new TableFill("create_time", FieldFill.INSERT));
//    tableFillList.add(new TableFill("modified_time", FieldFill.INSERT_UPDATE));
//    return tableFillList;
//  }

//  private AutoGenerator filterStrategy(AutoGenerator autoGenerator) {
//    InjectionConfig injectionConfig = autoGenerator.getCfg();
//    injectionConfig.setFileOutConfigList(injectionConfig.getFileOutConfigList().stream().filter(fileOutConfig -> {
//      if (!strategyConfig.isControllerInsert() && !strategyConfig.isControllerUpdate()) {
//        if (PathCons.BO_TEMPLATES_PATH.equals(fileOutConfig.getTemplatePath())) {
//          return false;
//        }
//      }
//      if (!strategyConfig.isControllerQuery()) {
//        return !PathCons.DTO_TEMPLATES_PATH.equals(fileOutConfig.getTemplatePath());
//      }
//      return true;
//    }).collect(Collectors.toList()));
//    return autoGenerator;
//  }
    public void execute() {
        new AutoGenerator()
                // 默认使用freemarker作为模板引擎
                .setTemplateEngine(new FreemarkerTemplateEngine())
                // 全局配置
                .setGlobalConfig(getGlobalConfig())
                // 数据源配置
                .setDataSource(getDataSourceConfig())
                // 策略配置
                .setStrategy(getStrategyConfig())
                // 包配置
                .setPackageInfo(getPackageConfig())
                // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
                .setCfg(getInjectionConfig())
                .setTemplate(getTemplateConfig()).execute();

        System.err.println(" TableName【 " + String.join(",", getStrategyConfig().getInclude()) + " 】"
                + "Generator Success !");
    }

}

