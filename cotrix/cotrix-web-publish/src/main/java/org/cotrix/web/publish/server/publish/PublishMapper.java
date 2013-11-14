/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import static org.cotrix.domain.dsl.Codes.*;

import javax.inject.Inject;

import org.cotrix.common.Outcome;
import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.LanguageClause;
import org.cotrix.io.MapService;
import org.cotrix.io.tabular.map.AttributeDirectives;
import org.cotrix.io.tabular.map.Codelist2TableDirectives;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.share.server.util.ValueUtils;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishMapper<T> {
	
	public Outcome<T> map(PublishDirectives publishDirectives);
	
	public class CsvMapper implements PublishMapper<Table> {
		
		@Inject
		protected MapService mapper;
		
		@Inject
		protected CodelistRepository repository;

		@Override
		public Outcome<Table> map(PublishDirectives publishDirectives) {
			
			Codelist2TableDirectives directives = new Codelist2TableDirectives();
			
			for (AttributeMapping mapping:publishDirectives.getMappings()) {
				if (mapping.isMapped()) {
					Attribute template = getTemplate(mapping.getAttributeDefinition());
					directives.add(AttributeDirectives.map(template).to(mapping.getColumnName()));
				}
			}
			
			directives.mode(convertMappingMode(publishDirectives.getMappingMode()));
			
			Codelist codelist = repository.lookup(publishDirectives.getCodelistId());
			
			return mapper.map(codelist, directives);

		}
		
		protected Codelist2TableDirectives getColumnDirectives(AttributeMapping mapping) {
			
			Attribute template = getTemplate(mapping.getAttributeDefinition());
			Codelist2TableDirectives directive = new Codelist2TableDirectives();
			directive.add(template);
			directive.codeColumnName(mapping.getColumnName());			
			return directive;
		}
		
		protected Attribute getTemplate(AttributeDefinition definition) {
			LanguageClause attributeBuilder = attr().name(ValueUtils.toQName(definition.getName())).value(null).ofType(ValueUtils.toQName(definition.getType()));
			if (definition.getLanguage()!=null && !definition.getLanguage().isEmpty()) return attributeBuilder.in(definition.getLanguage()).build();
			return attributeBuilder.build();
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
	
	public class SdmxMapper implements PublishMapper<CodelistBean> {
		
		@Inject
		protected MapService mapper;

		@Override
		public Outcome<CodelistBean> map(PublishDirectives publishDirectives) {
			
			/*Sdmx2CodelistDirectives directives = new Sdmx2CodelistDirectives();
			
			for (AttributeMapping mapping:mappings) setDirective(directives, mapping);
			
			directives.name(metadata.getName());
			directives.version(metadata.getVersion());
			
			return mapper.map(codelist, directives);*/
			return null;
		}
		
	/*	protected void setDirective(Sdmx2CodelistDirectives directives, AttributeMapping mapping) {
			SdmxElement element = SdmxElement.valueOf(mapping.getField().getId());
			if (mapping.isMapped()) {
				AttributeDefinition definition = mapping.getAttributeDefinition();
				directives.map(element, new QName(definition.getName()));
			} else directives.ignore(element);
		}*/
		
	}
}
