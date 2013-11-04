/**
 * 
 */
package org.cotrix.web.share.server.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.Container;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;
import org.cotrix.web.share.shared.codelist.UIAttribute;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Codelists {
	
	public static final Comparator<Codelist> NAME_COMPARATOR = new Comparator<Codelist>() {

		@Override
		public int compare(Codelist o1, Codelist o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.name().getLocalPart(), o2.name().getLocalPart());
		}
	};
	
	public static final Comparator<Codelist> VERSION_COMPARATOR = new Comparator<Codelist>() {

		@Override
		public int compare(Codelist o1, Codelist o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.version(), o2.version());
		}
	};
	
	public static CodelistMetadata toCodelistMetadata(Codelist codelist)
	{
		CodelistMetadata metadata = new CodelistMetadata();
		metadata.setId(codelist.id());
		metadata.setName(ValueUtils.safeValue(codelist.name()));
		metadata.setVersion(codelist.version());
		metadata.setAttributes(toUIAttributes(codelist.attributes()));
		return metadata;
	}
	
	public static List<UIAttribute> toUIAttributes(List<? extends Attribute> attributes)
	{
		return toUIAttributes(attributes, attributes.size());
	}
	
	public static List<UIAttribute> toUIAttributes(Container<? extends Attribute> attributesContainer)
	{
		return toUIAttributes(attributesContainer, attributesContainer.size());
	}
	
	public static List<UIAttribute> toUIAttributes(Iterable<? extends Attribute> attributesContainer, int size)
	{
		List<UIAttribute> attributes = new ArrayList<UIAttribute>(size);
		
		for (Attribute domainAttribute:attributesContainer) {
			UIAttribute attribute = toUIAttribute(domainAttribute);
			attributes.add(attribute);
		}
		
		return attributes;
	}
	
	public static UIAttribute toUIAttribute(Attribute attribute)
	{
		UIAttribute uiattribute = new UIAttribute();
		uiattribute.setName(ValueUtils.safeValue(attribute.name()));
		uiattribute.setType(ValueUtils.safeValue(attribute.type()));
		uiattribute.setLanguage(ValueUtils.safeValue(attribute.language()));
		uiattribute.setValue(ValueUtils.safeValue(attribute.value()));
		uiattribute.setId(ValueUtils.safeValue(attribute.id()));
		return uiattribute;
	}


}