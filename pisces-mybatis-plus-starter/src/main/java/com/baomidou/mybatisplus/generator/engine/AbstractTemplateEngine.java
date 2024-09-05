package com.baomidou.mybatisplus.generator.engine;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.jeasy.random.randomizers.misc.BooleanRandomizer;
import org.jeasy.random.randomizers.number.DoubleRandomizer;
import org.jeasy.random.randomizers.number.FloatRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.baomidou.mybatisplus.generator.config.rules.DbColumnType.LOCAL_DATE;
import static com.baomidou.mybatisplus.generator.config.rules.DbColumnType.LOCAL_DATE_TIME;

/**
 * @author: snake
 * @create-time: 2024-09-04
 * @description: 模板引擎抽象类 增加.http文件生成
 * @version: 1.0
 */
public abstract class AbstractTemplateEngine {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractTemplateEngine.class);
    /**
     * 配置信息
     */
    private ConfigBuilder configBuilder;


    /**
     * 模板引擎初始化
     */
    public AbstractTemplateEngine init(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }

    private Map<String, Object> buildMetaObjectHandlerStr() {
        Map<String, Object> resultMap = new HashMap<>();
        Converter converter = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
        StringBuilder insertSB = new StringBuilder();
        StringBuilder updateSB = new StringBuilder();
        Map<String, String> fieldTypeMap = new HashMap<>();
        List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
        for (TableInfo tableInfo : tableInfoList) {
            List<TableField> tableFields = tableInfo.getFields();
            for (TableField tableField : tableFields) {
                String fillName = tableField.getFill();
                boolean isFill = FieldFill.INSERT.toString().equals(fillName)
                        || FieldFill.UPDATE.toString().equals(fillName)
                        || FieldFill.INSERT_UPDATE.toString().equals(fillName);
                if (isFill) {
                    String typeName = tableField.getColumnType().getType();
                    fieldTypeMap.put(tableField.getName(), typeName);
                }
            }
        }

        List<TableFill> tableFillList = getConfigBuilder().getStrategyConfig().getTableFillList();
        for (TableFill tableFill : tableFillList) {
            String fieldName = tableFill.getFieldName();
            if (FieldFill.INSERT == tableFill.getFieldFill()
                    || FieldFill.INSERT_UPDATE == tableFill.getFieldFill()
                    && fieldTypeMap.containsKey(fieldName)) {
                insertSB.append("    this.strictInsertFill(metaObject, \"")
                        .append(converter.convert(fieldName))
                        .append("\", ")
                        .append(fieldTypeMap.get(fieldName))
                        .append(".class, ")
                        .append(getFieldTypeStr(fieldTypeMap.get(fieldName)));
            }
            if (FieldFill.UPDATE == tableFill.getFieldFill()
                    || FieldFill.INSERT_UPDATE == tableFill.getFieldFill()
                    && fieldTypeMap.containsKey(fieldName)) {
                updateSB.append("    this.strictUpdateFill(metaObject, \"")
                        .append(converter.convert(fieldName))
                        .append("\", ")
                        .append(fieldTypeMap.get(fieldName))
                        .append(".class, ")
                        .append(getFieldTypeStr(fieldTypeMap.get(fieldName)));
            }
        }

        resultMap.put("insertMetaObjectHandlerStr", insertSB.toString());
        resultMap.put("updateMetaObjectHandlerStr", updateSB.toString());
        return resultMap;
    }

    private String getFieldTypeStr(String typeName) {
        if ("localdatetime".equalsIgnoreCase(typeName)
                || "localdate".equalsIgnoreCase(typeName)
                || "datetime".equalsIgnoreCase(typeName)
        ) {
            return typeName + ".now());\n";
        } else {
            return "\"\");\n";
        }
    }

    /**
     * 输出 java xml 文件
     */
    public AbstractTemplateEngine batchOutput() {
        try {
            List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();
            // modify by pisces
            Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
            // add by pisces
//            Map<String, Object> metaObjectHandlerMap = buildMetaObjectHandlerStr();
            for (TableInfo tableInfo : tableInfoList) {
                Map<String, Object> objectMap = getObjectMap(tableInfo);
                // add by pisces
//                objectMap.putAll(metaObjectHandlerMap);
                TemplateConfig template = getConfigBuilder().getTemplate();
                // 自定义内容
                InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
                if (null != injectionConfig) {
                    injectionConfig.initTableMap(tableInfo);
                    objectMap.put("cfg", injectionConfig.getMap());
                    List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (FileOutConfig foc : focList) {
                            if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
                                writerFile(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
                            }
                        }
                    }
                }
                // Mp.java
                String entityName = tableInfo.getEntityName();
                if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
                    String entityFile = String.format((pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.ENTITY, entityFile)) {
                        writerFile(objectMap, templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())), entityFile);
                    }
                }
                // MpMapper.java
                if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
                    String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.MAPPER, mapperFile)) {
                        writerFile(objectMap, templateFilePath(template.getMapper()), mapperFile);
                    }
                }
                // MpMapper.xml
                if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
                    String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
                    if (isCreate(FileType.XML, xmlFile)) {
                        writerFile(objectMap, templateFilePath(template.getXml()), xmlFile);
                    }
                }
                // IMpService.java
                if (null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
                    String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator + tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE, serviceFile)) {
                        writerFile(objectMap, templateFilePath(template.getService()), serviceFile);
                    }
                }
                // MpServiceImpl.java
                if (null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
                    String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator + tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.SERVICE_IMPL, implFile)) {
                        writerFile(objectMap, templateFilePath(template.getServiceImpl()), implFile);
                    }
                }
                // MpController.java
                if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {
                    String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + suffixJavaOrKt()), entityName);
                    if (isCreate(FileType.CONTROLLER, controllerFile)) {
                        writerFile(objectMap, templateFilePath(template.getController()), controllerFile);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }

    protected void writerFile(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        if (StringUtils.isNotBlank(templatePath)) {
            this.writer(objectMap, templatePath, outputFile);
        }
    }

    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception;

    /**
     * 处理输出目录
     */
    public AbstractTemplateEngine mkdirs() {
        getConfigBuilder().getPathInfo().forEach((key, value) -> {
            File dir = new File(value);
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    logger.debug("创建目录： [" + value + "]");
                }
            }
        });
        return this;
    }


    /**
     * 打开输出目录
     */
    public void open() {
        String outDir = getConfigBuilder().getGlobalConfig().getOutputDir();
        if (getConfigBuilder().getGlobalConfig().isOpen()
                && StringUtils.isNotBlank(outDir)) {
            try {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + outDir);
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + outDir);
                    } else {
                        logger.debug("文件输出目录:" + outDir);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 渲染对象 MAP 信息
     *
     * @param tableInfo 表信息对象
     * @return ignore
     */
    public Map<String, Object> getObjectMap(TableInfo tableInfo) {
        Map<String, Object> objectMap;
        ConfigBuilder config = getConfigBuilder();
        if (config.getStrategyConfig().isControllerMappingHyphenStyle()) {
            objectMap = CollectionUtils.newHashMapWithExpectedSize(33);
            objectMap.put("controllerMappingHyphenStyle", config.getStrategyConfig().isControllerMappingHyphenStyle());
            objectMap.put("controllerMappingHyphen", StringUtils.camelToHyphen(tableInfo.getEntityPath()));
        } else {
            objectMap = CollectionUtils.newHashMapWithExpectedSize(31);
        }
        objectMap.put("restControllerStyle", config.getStrategyConfig().isRestControllerStyle());
        objectMap.put("config", config);
        objectMap.put("package", config.getPackageInfo());
        GlobalConfig globalConfig = config.getGlobalConfig();
        objectMap.put("author", globalConfig.getAuthor());
        objectMap.put("idType", globalConfig.getIdType() == null ? null : globalConfig.getIdType().toString());
        objectMap.put("logicDeleteFieldName", config.getStrategyConfig().getLogicDeleteFieldName());
        objectMap.put("versionFieldName", config.getStrategyConfig().getVersionFieldName());
        objectMap.put("activeRecord", globalConfig.isActiveRecord());
        objectMap.put("kotlin", globalConfig.isKotlin());
        objectMap.put("swagger3", globalConfig.isSwagger3());
        objectMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        objectMap.put("table", tableInfo);
        objectMap.put("enableCache", globalConfig.isEnableCache());
        objectMap.put("baseResultMap", globalConfig.isBaseResultMap());
        objectMap.put("baseColumnList", globalConfig.isBaseColumnList());
        objectMap.put("entity", tableInfo.getEntityName());
        objectMap.put("entitySerialVersionUID", config.getStrategyConfig().isEntitySerialVersionUID());
        objectMap.put("entityColumnConstant", config.getStrategyConfig().isEntityColumnConstant());
        objectMap.put("entityBuilderModel", config.getStrategyConfig().isEntityBuilderModel());
        objectMap.put("chainModel", config.getStrategyConfig().isChainModel());
        objectMap.put("entityLombokModel", config.getStrategyConfig().isEntityLombokModel());
        objectMap.put("entityBooleanColumnRemoveIsPrefix", config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix());
        objectMap.put("superEntityClass", getSuperClassName(config.getStrategyConfig().getSuperEntityClass()));
        objectMap.put("superMapperClassPackage", config.getStrategyConfig().getSuperMapperClass());
        objectMap.put("superMapperClass", getSuperClassName(config.getStrategyConfig().getSuperMapperClass()));
        objectMap.put("superServiceClassPackage", config.getStrategyConfig().getSuperServiceClass());
        objectMap.put("superServiceClass", getSuperClassName(config.getStrategyConfig().getSuperServiceClass()));
        objectMap.put("superServiceImplClassPackage", config.getStrategyConfig().getSuperServiceImplClass());
        objectMap.put("superServiceImplClass", getSuperClassName(config.getStrategyConfig().getSuperServiceImplClass()));
        objectMap.put("superControllerClassPackage", verifyClassPacket(config.getStrategyConfig().getSuperControllerClass()));
        objectMap.put("superControllerClass", getSuperClassName(config.getStrategyConfig().getSuperControllerClass()));

        // 用于.http
        objectMap.put("entityPath", Character.isLowerCase(
                tableInfo.getEntityName().charAt(0)) ? tableInfo.getEntityName() : Character.toLowerCase(tableInfo.getEntityName().charAt(0)) + tableInfo.getEntityName().substring(1));
        // 插入参数
        JSONObject insertJsonObject = getInsertJsonObject(tableInfo.getFields(), tableInfo.getCommonFields());
        objectMap.put("httpPkValue", insertJsonObject.getString("http_pk_value"));
        objectMap.put("httpPkName", insertJsonObject.getString("http_pk_name"));
        insertJsonObject.remove("http_pk_value");
        insertJsonObject.remove("http_pk_name");
        objectMap.put("insertJsonStr", JSONUtil.formatJsonStr(insertJsonObject.toJSONString()));
        // 更新参数，不含主键
        JSONObject updateJsonObject = getUpdateJsonObject(tableInfo.getFields(), tableInfo.getCommonFields());
        objectMap.put("updateJsonStr", JSONUtil.formatJsonStr(updateJsonObject.toJSONString()));

        return Objects.isNull(config.getInjectionConfig()) ? objectMap : config.getInjectionConfig().prepareObjectMap(objectMap);
    }

    private JSONObject getInsertJsonObject(List<TableField> fields, List<TableField> commonFields) {
        return getRandomJsonObject(fields, commonFields, true);
    }

    private JSONObject getUpdateJsonObject(List<TableField> fields, List<TableField> commonFields) {
        return getRandomJsonObject(fields, commonFields, false);
    }

    private JSONObject getRandomJsonObject(List<TableField> fields, List<TableField> commonFields, boolean isInsert) {
        JSONObject jsonObject = new JSONObject();
        List<TableField> allFields = new ArrayList<>();
        allFields.addAll(fields);
        allFields.addAll(commonFields);
        Converter<String, String> converter = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
        for (TableField tableField : allFields) {
            String columnVal = "";
            if (DbColumnType.STRING == tableField.getColumnType()) {
                String typeStr = tableField.getType();
                int columnLenghtNum = 1;
                try {
                    columnLenghtNum = Integer.parseInt(
                            typeStr.substring(typeStr.indexOf(StringPool.LEFT_BRACKET) + 1, typeStr.indexOf(StringPool.RIGHT_BRACKET))
                    );
                } catch (Exception e) {
                    // do nothing
                }
                if (columnLenghtNum > 50) {
                    columnLenghtNum = 50;
                }
                StringRandomizer randomizer = StringRandomizer.aNewStringRandomizer(columnLenghtNum);
                columnVal = randomizer.getRandomValue();
            } else if (LOCAL_DATE == tableField.getColumnType()
                    || DbColumnType.DATE == tableField.getColumnType()) {
                columnVal = DateUtil.today();
            } else if (DbColumnType.LOCAL_TIME == tableField.getColumnType()) {
                columnVal = DateUtil.formatTime(DateUtil.date());
            } else if (DbColumnType.YEAR == tableField.getColumnType()) {
                columnVal = DateUtil.year(DateUtil.date()) + "";
            } else if (LOCAL_DATE_TIME == tableField.getColumnType()) {
                columnVal = LocalDateTimeUtil.format(DateUtil.toLocalDateTime(DateUtil.date()), DatePattern.UTC_SIMPLE_PATTERN);
            } else if (DbColumnType.LONG == tableField.getColumnType()) {
                columnVal = new Random().nextInt(Integer.MAX_VALUE) + "";
            } else if (DbColumnType.BOOLEAN == tableField.getColumnType()) {
                columnVal = BooleanRandomizer.aNewBooleanRandomizer().getRandomValue().toString();
            } else if (DbColumnType.INTEGER == tableField.getColumnType()) {
                columnVal = new Random().nextInt(Integer.MAX_VALUE) + "";
            } else if (DbColumnType.FLOAT == tableField.getColumnType()) {
                columnVal = NumberUtil.roundStr(FloatRandomizer.aNewFloatRandomizer().getRandomValue(), 1);
            } else if (DbColumnType.DOUBLE == tableField.getColumnType()
                    || DbColumnType.BIG_DECIMAL == tableField.getColumnType()) {
                columnVal = NumberUtil.roundStr(DoubleRandomizer.aNewDoubleRandomizer().getRandomValue(), 2);
            }

            if (tableField.isKeyFlag()) {
                if (isInsert) {
                    // 临时用于记录主键值，需要在输出到模板前删掉
                    jsonObject.put("http_pk_value", columnVal);
                    jsonObject.put("http_pk_name", converter.convert(tableField.getColumnName()));
                    jsonObject.put(converter.convert(tableField.getColumnName()), columnVal);
                }
            } else {
                jsonObject.put(converter.convert(tableField.getColumnName()), columnVal);
            }

        }
        return jsonObject;
    }

    /**
     * 用于渲染对象MAP信息 {@link #getObjectMap(TableInfo)} 时的superClassPacket非空校验
     *
     * @param classPacket ignore
     * @return ignore
     */
    private String verifyClassPacket(String classPacket) {
        return StringUtils.isBlank(classPacket) ? null : classPacket;
    }

    /**
     * 获取类名
     *
     * @param classPath ignore
     * @return ignore
     */
    private String getSuperClassName(String classPath) {
        if (StringUtils.isBlank(classPath)) {
            return null;
        }
        return classPath.substring(classPath.lastIndexOf(StringPool.DOT) + 1);
    }


    /**
     * 模板真实文件路径
     *
     * @param filePath 文件路径
     * @return ignore
     */
    public abstract String templateFilePath(String filePath);


    /**
     * 检测文件是否存在
     *
     * @return 文件是否存在
     */
    protected boolean isCreate(FileType fileType, String filePath) {
        ConfigBuilder cb = getConfigBuilder();
        // 自定义判断
        InjectionConfig ic = cb.getInjectionConfig();
        if (null != ic && null != ic.getFileCreate()) {
            return ic.getFileCreate().isCreate(cb, fileType, filePath);
        }
        // 全局判断【默认】
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            file.getParentFile().mkdirs();
        }
        return !exist || getConfigBuilder().getGlobalConfig().isFileOverride();
    }

    /**
     * 文件后缀
     */
    protected String suffixJavaOrKt() {
        return getConfigBuilder().getGlobalConfig().isKotlin() ? ConstVal.KT_SUFFIX : ConstVal.JAVA_SUFFIX;
    }


    public ConfigBuilder getConfigBuilder() {
        return configBuilder;
    }

    public AbstractTemplateEngine setConfigBuilder(ConfigBuilder configBuilder) {
        this.configBuilder = configBuilder;
        return this;
    }
}
