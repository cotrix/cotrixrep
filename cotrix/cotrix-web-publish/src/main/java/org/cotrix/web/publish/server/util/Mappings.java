/**
 * 
 */
package org.cotrix.web.publish.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.io.sdmx.SdmxElement;
import org.cotrix.repository.CodelistSummary;
import org.cotrix.web.common.server.util.ValueUtils;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.AttributesMappings;
import org.cotrix.web.publish.shared.Column;
import org.cotrix.web.publish.shared.UISdmxElement;
import org.cotrix.web.publish.shared.AttributeMapping.Mapping;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Mappings {
	
	public interface MappingProvider<T extends Mapping> {
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

	public static AttributesMappings getMappings(CodelistSummary summary, MappingProvider<?> provider, boolean includeCodelistMappings) {
		List<AttributeMapping> codelistMappings = includeCodelistMappings?getCodelistsMappings(summary, provider):new ArrayList<AttributeMapping>();
		List<AttributeMapping> codesMappings = getCodesMappings(summary, provider);
		return new AttributesMappings(codesMappings, codelistMappings);
	}
	
	private static List<AttributeMapping> getCodelistsMappings(CodelistSummary summary, MappingProvider<?> provider) {
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (QName attributeName:summary.codelistNames()) {
			for (QName attributeType : summary.codelistTypesFor(attributeName)) {
				Collection<String> languages = summary.codelistLanguagesFor(attributeName, attributeType);
				if (languages.isEmpty()) mappings.add(getAttributeMapping(attributeName, attributeType, Language.NONE, provider));
				else for (String language:languages) mappings.add(getAttributeMapping(attributeName, attributeType, ValueUtils.safeLanguage(language), provider));
			}
		}
		return mappings;
	}
	
	private static List<AttributeMapping> getCodesMappings(CodelistSummary summary, MappingProvider<?> provider) {
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (QName attributeName:summary.codeNames()) {
			for (QName attributeType : summary.codeTypesFor(attributeName)) {
				Collection<String> languages = summary.codeLanguagesFor(attributeName, attributeType);
				if (languages.isEmpty()) mappings.add(getAttributeMapping(attributeName, attributeType, Language.NONE, provider));
				else for (String language:languages) mappings.add(getAttributeMapping(attributeName, attributeType, ValueUtils.safeLanguage(language), provider));
			}
		}
		return mappings;
	}

	private static AttributeMapping getAttributeMapping(QName name, QName type, Language language, MappingProvider<?> provider) {
		AttributeDefinition attr = new AttributeDefinition();
		attr.setName(ValueUtils.safeValue(name));
		attr.setType(ValueUtils.safeValue(type));
		attr.setLanguage(language);

		Mapping mapping = provider.getMapping(name, type, language);
		AttributeMapping attributeMapping = new AttributeMapping();
		attributeMapping.setAttributeDefinition(attr);
		attributeMapping.setMapping(mapping);
		attributeMapping.setMapped(provider.isMapped(name, type, language));
		return attributeMapping;
	}

}
