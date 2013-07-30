/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.io.map.MapService;
import org.cotrix.io.map.Outcome;
import org.cotrix.io.sdmx.SdmxMapDirectives;
import org.cotrix.io.tabular.ColumnDirectives;
import org.cotrix.io.tabular.MappingMode;
import org.cotrix.io.tabular.TableMapDirectives;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ImporterMapper<T> {

	public Outcome map(List<AttributeMapping> mappings, T codelist);
	
	public class CsvMappper implements ImporterMapper<Table> {
		
		protected MapService mapper;

		/**
		 * @param mapper
		 */
		public CsvMappper(MapService mapper) {
			this.mapper = mapper;
		}

		@Override
		public Outcome map(List<AttributeMapping> mappings, Table codelist) {
			
			
			AttributeMapping codeAttribute = null;
			List<ColumnDirectives> columnDirectives = new ArrayList<ColumnDirectives>();
			
			for (AttributeMapping mapping:mappings) {
				if (mapping.isMapped() && mapping.getAttributeDefinition().getType()==AttributeType.CODE) codeAttribute = mapping;
				else columnDirectives.add(getColumn(mapping));
			}
			if (codeAttribute == null) throw new IllegalArgumentException("Missing code mapping");
			
			Column column = new Column(codeAttribute.getField().getId());
			
			TableMapDirectives directives = new TableMapDirectives(column);
			for (ColumnDirectives directive:columnDirectives) directives.add(directive);
			
			
			Outcome outcome = mapper.map(codelist, directives);
			return outcome;
		}
		
		protected ColumnDirectives getColumn(AttributeMapping mapping) {
			Column column = new Column(mapping.getField().getId());
			ColumnDirectives directive = new ColumnDirectives(column);
			if (mapping.isMapped()) {
				AttributeDefinition definition = mapping.getAttributeDefinition();				
				directive.name(definition.getName());
				directive.language(definition.getLanguage());
			} else directive.mode(MappingMode.IGNORE);
			
			return directive;
		}
		
	}
	
	public class SdmxMapDirectivesProducer implements ImporterMapper<SdmxMapDirectives> {

		@Override
		public Outcome map(List<AttributeMapping> mappings,
				SdmxMapDirectives codelist) {
			// TODO Auto-generated method stub
			return null;
		}

		
		
	}
}
