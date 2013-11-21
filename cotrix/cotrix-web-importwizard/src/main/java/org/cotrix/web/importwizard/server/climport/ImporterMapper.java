/**
 * 
 */
package org.cotrix.web.importwizard.server.climport;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.utils.Constants;
import org.cotrix.io.MapService;
import org.cotrix.io.sdmx.SdmxElement;
import org.cotrix.io.sdmx.map.Sdmx2CodelistDirectives;
import org.cotrix.io.tabular.map.ColumnDirectives;
import org.cotrix.io.tabular.map.Table2CodelistDirectives;
import org.cotrix.web.importwizard.shared.AttributeDefinition;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Column;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface ImporterMapper<T> {

	public Outcome<Codelist> map(ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode, T codelist);
	
	public class CsvMapper implements ImporterMapper<Table> {
		
		protected MapService mapper;

		/**
		 * @param mapper
		 */
		public CsvMapper(MapService mapper) {
			this.mapper = mapper;
		}

		@Override
		public Outcome<Codelist> map(ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode, Table codelist) {
			
			AttributeMapping codeAttribute = null;
			List<ColumnDirectives> columnDirectives = new ArrayList<ColumnDirectives>();
			
			for (AttributeMapping mapping:mappings) {
				if (mapping.isMapped() && (
						mapping.getAttributeDefinition().getType()==AttributeType.CODE ||
						mapping.getAttributeDefinition().getType()==AttributeType.OTHER_CODE)) codeAttribute = mapping;
				else columnDirectives.add(getColumn(mapping));
			}
			if (codeAttribute == null) throw new IllegalArgumentException("Missing code mapping");
			
			Column column = new Column(codeAttribute.getField().getId());
			
			Table2CodelistDirectives directives = new Table2CodelistDirectives(column);
			for (ColumnDirectives directive:columnDirectives) directives.add(directive);
			
			directives.name(metadata.getName());
			directives.version(metadata.getVersion());
			
			directives.mode(convertMappingMode(mappingMode));
			
			return mapper.map(codelist, directives);
		}
		
		protected ColumnDirectives getColumn(AttributeMapping mapping) {
			Column column = new Column(mapping.getField().getId());
			ColumnDirectives directive = new ColumnDirectives(column);
			if (mapping.isMapped()) {
				AttributeDefinition definition = mapping.getAttributeDefinition();				
				directive.name(definition.getName());
				directive.language(definition.getLanguage());
				directive.type(getType(definition.getType(), definition.getCustomType()));
			} else directive.mode(org.cotrix.io.tabular.map.MappingMode.IGNORE);
			
			return directive;
		}
		
		protected QName getType(AttributeType type, String customType)
		{
			switch (type) {
				case ANNOTATION: return Constants.ANNOTATION_TYPE;
				case DESCRIPTION: return Constants.DESCRIPTION_TYPE;
				case CODE: return Constants.DEFAULT_TYPE;
				case OTHER_CODE: return Constants.DEFAULT_TYPE;
				case OTHER: return new QName(Constants.NS, customType);
				default: throw new IllegalArgumentException("Unknow attribute type "+type);
			}
		}
		
		protected org.cotrix.io.tabular.map.MappingMode convertMappingMode(MappingMode mode)
		{
			if (mode == null) return null;
			switch (mode) {
				case IGNORE: return org.cotrix.io.tabular.map.MappingMode.IGNORE;
				case LOG: return org.cotrix.io.tabular.map.MappingMode.LOG;
				case STRICT: return org.cotrix.io.tabular.map.MappingMode.STRICT;
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
		public Outcome<Codelist> map(ImportMetadata metadata, List<AttributeMapping> mappings, MappingMode mappingMode, CodelistBean codelist) {
			
			Sdmx2CodelistDirectives directives = new Sdmx2CodelistDirectives();
			
			for (AttributeMapping mapping:mappings) setDirective(directives, mapping);
			
			directives.name(metadata.getName());
			directives.version(metadata.getVersion());
			
			return mapper.map(codelist, directives);
		}
		
		protected void setDirective(Sdmx2CodelistDirectives directives, AttributeMapping mapping) {
			SdmxElement element = SdmxElement.valueOf(mapping.getField().getId());
			if (mapping.isMapped()) {
				AttributeDefinition definition = mapping.getAttributeDefinition();
				directives.map(element, new QName(definition.getName()));
			} else directives.ignore(element);
		}
		
	}
}
