/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.io.map.MapService;
import org.cotrix.io.map.Outcome;
import org.cotrix.io.sdmx.SdmxMapDirectives;
import org.cotrix.io.sdmx.SdmxMapDirectives.SdmxElement;
import org.cotrix.io.tabular.ColumnDirectives;
import org.cotrix.io.tabular.TableMapDirectives;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ImporterMapper<T> {

	public Outcome map(List<AttributeMapping> mappings, MappingMode mappingMode, T codelist);
	
	public class CsvMapper implements ImporterMapper<Table> {
		
		protected MapService mapper;

		/**
		 * @param mapper
		 */
		public CsvMapper(MapService mapper) {
			this.mapper = mapper;
		}

		@Override
		public Outcome map(List<AttributeMapping> mappings, MappingMode mappingMode, Table codelist) {
			
			
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
			
			directives.mode(convertMappingMode(mappingMode));
			
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
			} else directive.mode(org.cotrix.io.tabular.MappingMode.IGNORE);
			
			return directive;
		}
		
		protected org.cotrix.io.tabular.MappingMode convertMappingMode(MappingMode mode)
		{
			if (mode == null) return null;
			switch (mode) {
				case IGNORE: return org.cotrix.io.tabular.MappingMode.IGNORE;
				case LOG: return org.cotrix.io.tabular.MappingMode.LOG;
				case STRICT: return org.cotrix.io.tabular.MappingMode.STRICT;
				default: throw new IllegalArgumentException("Uncovertible mapping mode "+mode);
			}
		}
		
	}
	
	public class SdmxMapper implements ImporterMapper<CodelistBean> {
		
		protected MapService mapper;

		/**
		 * @param mapper
		 */
		public SdmxMapper(MapService mapper) {
			this.mapper = mapper;
		}



		@Override
		public Outcome map(List<AttributeMapping> mappings, MappingMode mappingMode, CodelistBean codelist) {
			
			SdmxMapDirectives directives = new SdmxMapDirectives();
			
			for (AttributeMapping mapping:mappings) setDirective(directives, mapping);
			
			Outcome outcome = mapper.map(codelist, directives);
			return outcome;
		}
		
		protected void setDirective(SdmxMapDirectives directives, AttributeMapping mapping) {
			SdmxElement element = SdmxElement.valueOf(mapping.getField().getId());
			if (mapping.isMapped()) {
				AttributeDefinition definition = mapping.getAttributeDefinition();
				directives.map(element, new QName(definition.getName()));
			} else directives.ignore(element);
		}
		
	}
}
