/**
 * 
 */
package org.cotrix.web.importwizard.server.upload;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.io.sdmx.SdmxMapDirectives;
import org.cotrix.io.sdmx.SdmxMapDirectives.SdmxElement;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.cotrix.web.importwizard.shared.Field;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultMappingsGuesser implements MappingGuesser {
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public List<AttributeMapping> guessMappings(Table table)
	{
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		
		List<Column> columns = table.columns();
		
		boolean first = true;
		
		for (Column column:columns) {
			
			AttributeMapping attributeMapping = new AttributeMapping();
			
			Field field = getField(column);
			attributeMapping.setField(field);
			
			AttributeDefinition attributeDefinition = guessMapping(field, column, first);
			attributeMapping.setAttributeDefinition(attributeDefinition);
			
			mappings.add(attributeMapping);
			first = false;
		}
		
		return mappings;
	}
	
	protected Field getField(Column column)
	{
		Field field = new Field();
		field.setId(column.name().toString());
		field.setLabel(column.name().getLocalPart());
		return field;
	}
	
	protected AttributeDefinition guessMapping(Field field, Column column, boolean isFirst)
	{
		AttributeDefinition attributeDefinition = new AttributeDefinition();
		attributeDefinition.setName(column.name().getLocalPart());
		
		AttributeType type = isFirst?AttributeType.CODE:AttributeType.DESCRIPTION;
		attributeDefinition.setType(type);
		
		//TODO guess language
		
		return attributeDefinition;
	}

	@Override
	public List<AttributeMapping> getSdmxDefaultMappings() {
		
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		
		/*AttributeMapping codeMapping = getCodeMapping();
		mappings.add(codeMapping);*/
		
		for (SdmxElement sdmxElement:SdmxElement.values()) {
			AttributeMapping mapping = new AttributeMapping();
			mapping.setField(getField(sdmxElement));
			mapping.setAttributeDefinition(getDefinition(sdmxElement));
			mappings.add(mapping);
		}		
		return mappings;
	}
	
	protected AttributeMapping getCodeMapping()
	{
		AttributeMapping mapping = new AttributeMapping();
		Field field = new Field();
		field.setId("code");
		field.setLabel("code");
		mapping.setField(field);
		
		AttributeDefinition definition = new AttributeDefinition();
		definition.setName("code");
		definition.setType(AttributeType.CODE);
		mapping.setAttributeDefinition(definition);
		
		return mapping;
	}
	
	protected Field getField(SdmxElement element)
	{
		Field field = new Field();
		field.setId(element.toString());
		field.setLabel(element.name());
		return field;
	}
	
	protected AttributeDefinition getDefinition(SdmxElement element)
	{
		String name = SdmxMapDirectives.DEFAULT.get(element).toString();
		AttributeDefinition definition = new AttributeDefinition();
		definition.setName(name);
		definition.setType(AttributeType.DESCRIPTION);
		return definition;
	}

}
