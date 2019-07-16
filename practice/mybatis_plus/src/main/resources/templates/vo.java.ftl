package ${package.Entity}.vo;

import ${package.Entity}.${entity};
<#if entityLombokModel>
import java.io.Serializable;
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
@SuppressWarnings("serial")	
public class ${entity}Vo extends ${entity} implements Serializable{
	
	<#list table.fields as field>
		<#if field.propertyType == "LocalDateTime" || field.propertyType == "LocalDate" || field.propertyType == "LocalTime">
	/**
 	 * ${field.comment}Begin
 	 */
    private String ${field.propertyName}Begin;
    /**
 	 * ${field.comment}End
 	 */
    private String ${field.propertyName}End;
	    </#if>
	</#list>
    		
}