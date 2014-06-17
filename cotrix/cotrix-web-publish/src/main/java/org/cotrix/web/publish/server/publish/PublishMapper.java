/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.common.Outcome;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.AttributeGrammar.OptionalClause;
import org.cotrix.io.MapService;
import org.cotrix.io.comet.map.Codelist2CometDirectives;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.io.tabular.map.AttributeDirectives;
import org.cotrix.io.tabular.map.Codelist2TableDirectives;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.publish.server.util.SdmxElements;
import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.Column;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.PublishMetadata;
import org.cotrix.web.publish.shared.UISdmxElement;
import org.fao.fi.comet.mapping.model.MappingData;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtualrepository.tabular.Table;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface PublishMapper<T> {
	
	public Outcome<T> map(Codelist codelist,PublishDirectives publishDirectives);
	
	public class CsvMapper implements PublishMapper<Table> {
		
		protected Logger logger = LoggerFactory.getLogger(CsvMapper.class);
		
		@Inject
		protected MapService mapper;
		
		@Inject
		protected CodelistRepository repository;

		@Override
		public Outcome<Table> map(Codelist codelist,PublishDirectives publishDirectives) {
			
			Codelist2TableDirectives directives = new Codelist2TableDirectives();
			
			for (AttributeMapping mapping:publishDirectives.getMappings().getCodesAttributesMapping()) {
				if (mapping.isMapped()) {
					Attribute template = getTemplate(mapping.getAttributeDefinition());
					Column column = (Column) mapping.getMapping();
					logger.trace("mapping {} to {}", template, column.getName());
					directives.add(AttributeDirectives.map(template).to(column.getName()));
				}
			}
			
			directives.mode(convertMappingMode(publishDirectives.getMappingMode()));
			
			return mapper.map(codelist, directives);

		}
		
		protected Attribute getTemplate(AttributeDefinition definition) {
			OptionalClause attributeBuilder = attribute().name(ValueUtils.toQName(definition.getName())).value(null).ofType(ValueUtils.toQName(definition.getType()));
			if (definition.getLanguage()!=null && definition.getLanguage()!=Language.NONE) return attributeBuilder.in(definition.getLanguage().getCode()).build();
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
		
		protected Logger logger = LoggerFactory.getLogger(SdmxMapper.class);
		
		@Inject
		protected MapService mapper;
		
		@Inject
		protected CodelistRepository repository;

		@Override
		public Outcome<CodelistBean> map(Codelist codelist, PublishDirectives publishDirectives) {
			
			Codelist2SdmxDirectives directives = new Codelist2SdmxDirectives();
			
			PublishMetadata metadata = publishDirectives.getMetadata();
			//FIXME directives.agency(metadata.get);
			directives.name(metadata.getName().getLocalPart());
			directives.version(metadata.getVersion());
			directives.isFinal(metadata.isSealed());
			
			for (AttributeMapping mapping:publishDirectives.getMappings().getCodelistAttributesMapping()) {
				if (mapping.isMapped()) {
					
					AttributeDefinition attributeDefinition = mapping.getAttributeDefinition();
					UISdmxElement element = (UISdmxElement) mapping.getMapping();
					logger.trace("mapping {} to {}", attributeDefinition, element);
					directives.map(ValueUtils.toQName(attributeDefinition.getName()), ValueUtils.toQName(attributeDefinition.getType())).
							   to(SdmxElements.toSdmxElement(element))
							   .forCodelist();
				}
			}
			
			for (AttributeMapping mapping:publishDirectives.getMappings().getCodesAttributesMapping()) {
				if (mapping.isMapped()) {
					
					AttributeDefinition attributeDefinition = mapping.getAttributeDefinition();
					UISdmxElement element = (UISdmxElement) mapping.getMapping();
					logger.trace("mapping {} to {}", attributeDefinition, element);
					directives.map(ValueUtils.toQName(attributeDefinition.getName()), ValueUtils.toQName(attributeDefinition.getType())).
							   to(SdmxElements.toSdmxElement(element))
							   .forCodes();
				}
			}
			
			
			return mapper.map(codelist, directives);
		}		
	}
	
	public class CometMapper implements PublishMapper<MappingData> {
		
		protected Logger logger = LoggerFactory.getLogger(CometMapper.class);
		
		@Inject
		protected MapService mapper;

		@Override
		public Outcome<MappingData> map(Codelist codelist, PublishDirectives publishDirectives) {
			
			List<QName> targets = new ArrayList<>(); 
			
			for (AttributeMapping mapping:publishDirectives.getMappings().getCodesAttributesMapping()) {
				if (mapping.isMapped()) targets.add(ValueUtils.toQName(mapping.getAttributeDefinition().getName()));
			}
			
			return mapper.map(codelist, new Codelist2CometDirectives(targets));
		}		
	}
}
