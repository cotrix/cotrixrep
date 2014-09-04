/**
 * 
 */
package org.cotrix.web.publish.server.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.links.AttributeLink;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.links.LinkOfLink;
import org.cotrix.domain.utils.AttributeTemplate;
import org.cotrix.domain.utils.DomainConstants;
import org.cotrix.io.sdmx.SdmxElement;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.publish.shared.Column;
import org.cotrix.web.publish.shared.DefinitionMapping;
import org.cotrix.web.publish.shared.DefinitionMapping.MappingTarget;
import org.cotrix.web.publish.shared.DefinitionsMappings;
import org.cotrix.web.publish.shared.UIDefinition;
import org.cotrix.web.publish.shared.UIDefinition.DefinitionType;
import org.cotrix.web.publish.shared.UISdmxElement;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Mappings {

	public interface MappingProvider<T extends MappingTarget> {
		public T getMapping(QName name, QName type, Language language);
		public boolean isMapped(QName name, QName type, Language language);
	}

	public static final MappingProvider<Column> COLUMN_PROVIDER = new MappingProvider<Column>() {

		@Override
		public Column getMapping(QName name, QName type, Language language) {
			StringBuilder columnName = new StringBuilder(name.getLocalPart());
			if (language!=Language.NONE) columnName.append(" (").append(language.getCode()).append(')');
			Column column = new Column();
			column.setName(columnName.toString());
			return column;
		}

		@Override
		public boolean isMapped(QName name, QName type, Language language) {
			return true;
		}
	};

	public static final MappingProvider<UISdmxElement> SDMX_PROVIDER = new MappingProvider<UISdmxElement>() {

		@Override
		public UISdmxElement getMapping(QName name, QName type, Language language) {
			SdmxElement element = SdmxElements.findSdmxElement(name, type);
			return SdmxElements.toUISdmxElement(element);
		}

		@Override
		public boolean isMapped(QName name, QName type, Language language) {
			return true;
		}
	};

	public static final MappingProvider<Column> COMET_PROVIDER = new MappingProvider<Column>() {

		@Override
		public Column getMapping(QName name, QName type, Language language) {
			return null;
		}

		@Override
		public boolean isMapped(QName name, QName type, Language language) {
			return false;
		}
	};

	public static DefinitionsMappings getMappings(Codelist codelist, MappingProvider<?> provider, boolean includeCodelistMappings) {
		List<DefinitionMapping> codelistMappings = includeCodelistMappings?getCodelistsMappings(codelist, provider):new ArrayList<DefinitionMapping>();
		List<DefinitionMapping> codesMappings = getCodesMappings(codelist, provider);
		return new DefinitionsMappings(codesMappings, codelistMappings);
	}

	private static List<DefinitionMapping> getCodelistsMappings(Codelist codelist, MappingProvider<?> provider) {
		List<DefinitionMapping> mappings = new ArrayList<DefinitionMapping>();
		for (Attribute attribute:codelist.attributes()) {
			
			AttributeDefinition definition = attribute.definition();
			
			//skip system attributes
			if (definition.is(DomainConstants.SYSTEM_TYPE)) continue;
			
			DefinitionMapping mapping = getDefinitionMapping(definition, provider);
			
			//we use the attribute id because the definition is not shared
			mapping.getDefinition().setId(attribute.id());
			mappings.add(mapping);			
		}
		return mappings;
	}

	private static List<DefinitionMapping> getCodesMappings(Codelist codelist, MappingProvider<?> provider) {
		List<DefinitionMapping> mappings = new ArrayList<DefinitionMapping>();
		for (AttributeDefinition definition:codelist.attributeDefinitions()) {
			mappings.add(getDefinitionMapping(definition, provider));
		}

		for (LinkDefinition definition:codelist.linkDefinitions()) {
			mappings.add(getLinkMapping(definition, provider));
		}
		return mappings;
	}

	private static DefinitionMapping getDefinitionMapping(AttributeDefinition definition, MappingProvider<?> provider) {

		MappingTarget mapping = provider.getMapping(definition.qname(), definition.type(), ValueUtils.safeLanguage(definition.language()));
		DefinitionMapping attributeMapping = new DefinitionMapping();
		attributeMapping.setDefinition(getDefinition(definition));
		attributeMapping.setTarget(mapping);
		attributeMapping.setMapped(provider.isMapped(definition.qname(), definition.type(), ValueUtils.safeLanguage(definition.language())));
		return attributeMapping;
	}

	public static UIDefinition getDefinition(AttributeDefinition def) {
		String title = ValueUtils.getSafeLocalPart(def.qname());
		String subTitle = join(", ", ValueUtils.getSafeLocalPart(def.type()), ValueUtils.safeLanguage(def.language()).getCode());
		return new UIDefinition(def.id(), DefinitionType.ATTRIBUTE_DEFINITION, title, subTitle);
	}

	private static DefinitionMapping getLinkMapping(LinkDefinition definition, MappingProvider<?> provider) {

		//TODO complete me
		MappingTarget mapping = provider.getMapping(definition.qname(), null, Language.NONE);
		DefinitionMapping attributeMapping = new DefinitionMapping();
		attributeMapping.setDefinition(getDefinition(definition));
		attributeMapping.setTarget(mapping);
		attributeMapping.setMapped(provider.isMapped(definition.qname(), null, Language.NONE));
		return attributeMapping;
	}

	public static UIDefinition getDefinition(LinkDefinition def) {
		String title = ValueUtils.getSafeLocalPart(def.qname());
		String subTitle = getNameAndVersion(def.target());

		if (def.valueType() instanceof AttributeLink) {
			AttributeLink attributeLink = (AttributeLink) def.valueType();
			AttributeTemplate template = attributeLink.template();
			subTitle += ", "+ join(", ", ValueUtils.getSafeLocalPart(template.name()), ValueUtils.getSafeLocalPart(template.type()), ValueUtils.safeLanguage(template.language()).getCode());
		}
		
		if (def.valueType() instanceof LinkOfLink) {
			LinkOfLink linkOfLink = (LinkOfLink) def.valueType();
			LinkDefinition definition = linkOfLink.target();
			subTitle += ", "+ join(", ", ValueUtils.getSafeLocalPart(definition.qname()), getNameAndVersion(definition.target()));
		}

		return new UIDefinition(def.id(), DefinitionType.LINK_DEFINITION, title, subTitle);
	}
	
	private static String join(String separator, String ... tokens) {
		StringBuilder join = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if (token.isEmpty()) continue;
			if (join.length() != 0)	join.append(separator);
			join.append(token);
			
		}
		return join.toString();		
	}
	
	private static String getNameAndVersion(Codelist codelist) {
		return ValueUtils.getSafeLocalPart(codelist.qname()) + " " + codelist.version();
	}

}
