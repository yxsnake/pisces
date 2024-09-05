package ${cfg.bo};


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.math.BigDecimal;
<#if swagger3>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>
import io.github.yxsnake.pisces.web.core.converter.Convert;

/**
 *
 * @author ${author}
 * @since ${date}
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
<#if superEntityClass??>
public class ${entity}BO extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity}BO extends Model<${entity}> {
<#else>
<#if swagger3>
@Schema(name="${entity}业务对象")
</#if>
public class ${entity}BO implements Convert,Serializable {
</#if>

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
        <#if swagger3>
    @Schema(description = "${field.comment}")
        <#else>
          /**
          * ${field.comment}
          */
        </#if>
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

    public interface Insert {}

    public interface Update {}

}






