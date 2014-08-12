/**
 * 
 */
package org.cotrix.web.publish.server.publish;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.namespace.QName;

import org.cotrix.common.Outcome;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.trait.Definition;
import org.cotrix.io.MapService;
import org.cotrix.io.comet.map.Codelist2CometDirectives;
import org.cotrix.io.sdmx.map.Codelist2SdmxDirectives;
import org.cotrix.io.tabular.map.Codelist2TableDirectives;
import org.cotrix.io.tabular.map.MemberDirective;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.web.publish.server.util.SdmxElements;
import org.cotrix.web.publish.shared.Column;
import org.cotrix.web.publish.shared.DefinitionMapping;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.PublishMetadata;
import org.cotrix.web.publish.shared.UIDefinition;
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
			
			for (DefinitionMapping mapping:publishDirectives.getMappings().getCodesAttributesMapping()) {
				if (mapping.isMapped()) {
					Definition definition = getDefinition(codelist, mapping.getDefinition());
					Column column = (Column) mapping.getTarget();
					logger.trace("mapping {} to {}", mapping.getDefinition(), column.getName());
					directives.add(MemberDirective.map(definition).to(column.getName()));
				}
			}
			
			directives.mode(convertMappingMode(publishDirectives.getMappingMode()));
			
			return mapper.map(codelist, directives);

		}

		private org.cotrix.io.tabular.map.MappingMode convertMappingMode(MappingMode mode)
		{
			if (mode == null) return null;
			switch (mode) {
				case IGNORE: return org.cotrix.io.tabular.map.MappingMode.ignore;
				case LOG: return org.cotrix.io.tabular.map.MappingMode.log;
				case STRICT: return org.cotrix.io.tabular.map.MappingMode.strict;
				default: throw new IllegalArgumentException("Uncovertible mapping mode "+mode);
			}
		}
		
		private Definition getDefinition(Codelist codelist, UIDefinition definition) {
			switch (definition.getDefinitionType()) {
				case ATTRIBUTE_DEFINITION: return codelist.attributeDefinitions().lookup(definition.getId());
				case LINK_DEFINITION: return codelist.linkDefinitions().lookup(definition.getId());
			}
			return null;
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
			directives.id(metadata.getName().getLocalPart());
			directives.version(metadata.getVersion());
			directives.isFinal(metadata.isSealed());
			

			for (DefinitionMapping mapping:publishDirectives.getMappings().getCodelistAttributesMapping()) {
				if (mapping.isMapped()) {
					
					Definition definition = getCodelistDefinition(codelist, mapping.getDefinition());
					UISdmxElement element = (UISdmxElement) mapping.getTarget();
					
					logger.trace("mapping {} to {}", mapping.getDefinition(), element);
					
					directives.map(definition).to(SdmxElements.toSdmxElement(element)).forCodelist();
				}
			}
			
			for (DefinitionMapping mapping:publishDirectives.getMappings().getCodesAttributesMapping()) {
				if (mapping.isMapped()) {
					
					Definition definition = getCodeDefinition(codelist, mapping.getDefinition());
					
					UISdmxElement element = (UISdmxElement) mapping.getTarget();
					logger.trace("mapping {} to {}", mapping.getDefinition(), element);
					directives.map(definition).to(SdmxElements.toSdmxElement(element)).forCodes();
				}
			}
			
			return mapper.map(codelist, directives);
		}
		
		private Definition getCodeDefinition(Codelist codelist, UIDefinition definition) {
			switch (definition.getDefinitionType()) {
				case ATTRIBUTE_DEFINITION: return codelist.attributeDefinitions().lookup(definition.getId());
				case LINK_DEFINITION: return codelist.linkDefinitions().lookup(definition.getId());
			}
			return null;
		}
		
		private Definition getCodelistDefinition(Codelist codelist, UIDefinition definition) {
			switch (definition.getDefinitionType()) {
				case ATTRIBUTE_DEFINITION: return codelist.attributes().lookup(definition.getId()).definition();
				case LINK_DEFINITION: throw new IllegalArgumentException("The definition of an attribute for the codelist can't refers to a link");
			}
			return null;
		}
	}
	
	public class CometMapper implements PublishMapper<MappingData> {
		
		protected Logger logger = LoggerFactory.getLogger(CometMapper.class);
		
		@Inject
		protected MapService mapper;

		@Override
		public Outcome<MappingData> map(Codelist codelist, PublishDirectives publishDirectives) {
			
			List<QName> targets = new ArrayList<>(); 
			
			for (DefinitionMapping mapping:publishDirectives.getMappings().getCodesAttributesMapping()) {
				if (mapping.isMapped()) targets.add(codelist.attributeDefinitions().lookup(mapping.getDefinition().getId()).qname());
			}
			
			Codelist2CometDirectives cometDirectives = new Codelist2CometDirectives();
			cometDirectives.targetAttributes().addAll(targets);
			
			return mapper.map(codelist, cometDirectives);
		}		
	}
}
