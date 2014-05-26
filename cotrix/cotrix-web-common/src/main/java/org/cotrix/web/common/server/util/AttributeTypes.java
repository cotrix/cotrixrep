/**
 * 
 */
package org.cotrix.web.common.server.util;

import org.cotrix.domain.attributes.Definition;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypes {
	
	public static UIAttributeType toUIAttributeType(Definition definition) {
		UIAttributeType attributeType = new UIAttributeType();
		attributeType.setId(definition.id());
		attributeType.setLanguage(ValueUtils.safeLanguage(definition.language()));
		attributeType.setName(ValueUtils.safeValue(definition.name()));
		attributeType.setType(ValueUtils.safeValue(definition.type()));
		return attributeType;
	}

}
