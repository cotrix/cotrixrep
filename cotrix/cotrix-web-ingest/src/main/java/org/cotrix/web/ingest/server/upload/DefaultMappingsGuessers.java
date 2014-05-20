/**
 * 
 */
package org.cotrix.web.ingest.server.upload;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.io.sdmx.SdmxElement;
import org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.ingest.shared.AttributeDefinition;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.AttributeType;
import org.cotrix.web.ingest.shared.Field;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultMappingsGuessers {

	public static class TableMappingGuesser implements MappingGuesser {

		private static final long serialVersionUID = 2651521163388760105L;
		
		private Table table;
		
		public TableMappingGuesser(Table table) {
			this.table = table;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public List<AttributeMapping> guessMappings(List<String> headers)
		{
			List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();

			List<Column> columns = table.columns();

			boolean first = true;
			int index = 0;

			for (Column column:columns) {
				
				String header = index<headers.size()?headers.get(index):null;

				AttributeMapping attributeMapping = new AttributeMapping();

				Field field = getField(column);
				attributeMapping.setField(field);

				AttributeDefinition attributeDefinition = guessMapping(field, column, header, first);
				attributeMapping.setAttributeDefinition(attributeDefinition);

				mappings.add(attributeMapping);
				first = false;
				index++;
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

		protected AttributeDefinition guessMapping(Field field, Column column, String header, boolean isFirst)
		{
			AttributeDefinition attributeDefinition = new AttributeDefinition();
			String name = header != null?header:column.name().getLocalPart();
			attributeDefinition.setName(name);

			AttributeType type = isFirst?AttributeType.CODE:AttributeType.DESCRIPTION;
			attributeDefinition.setType(type);

			//TODO guess language
			attributeDefinition.setLanguage(Language.NONE);

			return attributeDefinition;
		}

	}

	public static class SdmxMappingGuesser implements MappingGuesser {

		private static final long serialVersionUID = 8173566374046999383L;
		
		public static final SdmxMappingGuesser INSTANCE = new SdmxMappingGuesser();

		@Override
		public List<AttributeMapping> guessMappings(List<String> headers) {

			List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();

			for (SdmxElement sdmxElement:SdmxElement.values()) {
				AttributeMapping mapping = new AttributeMapping();
				mapping.setField(getField(sdmxElement));
				mapping.setAttributeDefinition(getDefinition(sdmxElement));
				mappings.add(mapping);
			}		
			return mappings;
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
			String name = Sdmx2CodelistDirectives.DEFAULT.get(element).getLocalPart();
			AttributeDefinition definition = new AttributeDefinition();
			definition.setName(name);
			definition.setType(AttributeType.DESCRIPTION);
			definition.setLanguage(Language.NONE);
			return definition;
		}
	}

}
