package ${package.Entity};

<#list table.importPackages as pkg>
import ${pkg};
</#list>
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
import com.baomidou.mybatisplus.annotation.TableField;
<#if entityLombokModel>
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>

/**
 * ${table.comment!}
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
@NoArgsConstructor
@AllArgsConstructor
    <#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
    <#else>
@EqualsAndHashCode(callSuper = false)
    </#if>
@Accessors(chain = true)
</#if>
<#if table.convert>
@TableName("${table.name}")
</#if>
<#if swagger2>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
<#if superEntityClass??>
public class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {
<#elseif activeRecord>
public class ${entity} extends Model<${entity}> {
<#else>
@SuppressWarnings("serial")	
public class ${entity} implements Serializable {
</#if>


<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
        <#if swagger2>
    /**
	 * ${field.comment}
	 */
	@ApiModelProperty(value = "${field.comment}")
        <#else>
	/**
	 * ${field.comment}
	 */
        </#if>
    </#if>
    <#if field.keyFlag>
    <#-- 主键 -->
        <#if field.keyIdentityFlag>
	@TableId(value = "${field.name}", type = IdType.AUTO)
        <#elseif idType??>
	@TableId(value = "${field.name}", type = IdType.${idType})
        <#elseif field.convert>
	@TableId("${field.name}")
        </#if>
    <#-- 普通字段 -->
    <#elseif field.fill??>
    <#-- -----   存在字段填充设置   ----->
        <#if field.convert>
	@TableField(value = "${field.name}", fill = FieldFill.${field.fill})
        <#else>
	@TableField(fill = FieldFill.${field.fill})
        </#if>
    <#elseif field.convert>
	@TableField("${field.name}")
	<#else>
	@TableField(value = "${field.name}")
    </#if>
<#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
	@Version
    </#if>
<#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
	@TableLogic
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
	public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
	}

        <#if entityBuilderModel>
	public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        <#else>
	public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
        </#if>
        this.${field.propertyName} = ${field.propertyName};
        <#if entityBuilderModel>
		return this;
        </#if>
	}
    </#list>
</#if>

<#if entityColumnConstant>
    <#list table.fields as field>
    <#if field.comment!?contains(":")&&field.comment!?contains("-")&&field.comment!?contains(";")>
    	<#assign constantStr=field.comment?substring(field.comment?index_of(":")+1)/>
    	<#assign elArr = constantStr?split(";")>
    	<#if elArr?size gt 0>
    		<#list elArr as elStr>
    			<#if elStr?length lte 0>
    			<#assign elArr = elArr[0..elStr_index-1]>
    			</#if>
    		</#list>
    	</#if>
    	<#if elArr?size lt 3>
    		<#list elArr as elStr>
    /**
     * ${field.comment?substring(0,field.comment?index_of(":"))}:${elStr}
     */
	public static final ${field.propertyType} ${field.name?upper_case}_${elStr?substring(0,elStr?index_of("-"))} = ${elStr?substring(0,elStr?index_of("-"))};
    		</#list>
    	<#else>
    public enum ${field.capitalName} {
    		<#list elArr as elStr>
		<#if elStr_index gt 0>,</#if>${elStr?substring(elStr?index_of("-")+1)}(${elStr?substring(0,elStr?index_of("-"))})
			</#list>
		;
		private int value;
		
		${field.capitalName}(int val){
			this.value = val;
		}
		
		public int getValue(){
			return value;
		}
		public void setValue(int val){
			this.value=val;
		}
	}
    	</#if>
	</#if>
    </#list>
</#if>
<#if activeRecord>
    @Override
    protected Serializable pkVal() {
    <#if keyPropertyName??>
        return this.${keyPropertyName};
    <#else>
        return null;
    </#if>
    }

</#if>
<#if !entityLombokModel>
    @Override
    public String toString() {
    return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
		"${field.propertyName}=" + ${field.propertyName} +
        <#else>
		", ${field.propertyName}=" + ${field.propertyName} +
        </#if>
    </#list>
    "}";
    }
</#if>
}
