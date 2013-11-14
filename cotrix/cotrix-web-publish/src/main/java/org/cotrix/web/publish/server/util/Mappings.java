/**
 * 
 */
package org.cotrix.web.publish.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.repository.CodelistSummary;
import org.cotrix.web.publish.shared.AttributeDefinition;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.Column;
import org.cotrix.web.publish.shared.SdmxElement;
import org.cotrix.web.publish.shared.AttributeMapping.Mapping;
import org.cotrix.web.share.server.util.ValueUtils;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Mappings {
	
	public interface MappingProvider<T extends Mapping> {
		public T getMapping(QName name, QName type, String language);
	}
	
	public static final MappingProvider<Column> COLUMN_PROVIDER = new MappingProvider<Column>() {

		@Override
		public Column getMapping(QName name, QName type, String language) {
			StringBuilder columnName = new StringBuilder(name.getLocalPart());
			if (language!=null) columnName.append('(').append(language).append(')');
			Column column = new Column();
			column.setName(columnName.toString());
			return column;
		}
	};
	
	public static final MappingProvider<SdmxElement> SDMX_PROVIDER = new MappingProvider<SdmxElement>() {

		@Override
		public SdmxElement getMapping(QName name, QName type, String language) {
			return SdmxElement.DESCRIPTION;
		}
	};

	
	public static List<AttributeMapping> getChannelMappings(CodelistSummary summary, MappingProvider<?> provider) {
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (QName attributeName:summary.codeNames()) {
			for (QName attributeType : summary.codeTypesFor(attributeName)) {
				Collection<String> languages = summary.codeLanguagesFor(attributeName, attributeType);
				if (languages.isEmpty()) mappings.add(getAttributeMapping(attributeName, attributeType, null, provider));
				else for (String language:languages) mappings.add(getAttributeMapping(attributeName, attributeType, language, provider));
			}
		}

		return mappings;
	}


	public static List<AttributeMapping> getFileMappings(CodelistSummary summary, MappingProvider<?> provider) {
		List<AttributeMapping> mappings = new ArrayList<AttributeMapping>();
		for (QName attributeName:summary.codeNames()) {
			for (QName attributeType : summary.codeTypesFor(attributeName)) {
				Collection<String> languages = summary.codeLanguagesFor(attributeName, attributeType);
				if (languages.isEmpty()) mappings.add(getAttributeMapping(attributeName, attributeType, null, provider));
				else for (String language:languages) mappings.add(getAttributeMapping(attributeName, attributeType, language, provider));
			}
		}
		return mappings;
	}

	public static AttributeMapping getAttributeMapping(QName name, QName type, String language, MappingProvider<?> provider) {
		AttributeDefinition attr = new AttributeDefinition();
		attr.setName(ValueUtils.safeValue(name));
		attr.setType(ValueUtils.safeValue(type));
		attr.setLanguage(ValueUtils.safeValue(language));

		Mapping mapping = provider.getMapping(name, type, language);
		AttributeMapping attributeMapping = new AttributeMapping();
		attributeMapping.setAttributeDefinition(attr);
		attributeMapping.setMapping(mapping);
		attributeMapping.setMapped(true);
		return attributeMapping;
	}

}
