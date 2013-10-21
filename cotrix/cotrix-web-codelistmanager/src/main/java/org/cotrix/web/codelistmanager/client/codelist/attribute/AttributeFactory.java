/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist.attribute;

import org.cotrix.web.codelistmanager.client.util.Constants;
import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.UIQName;

import com.google.gwt.dom.client.Document;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeFactory {
	
	@Inject
	protected static Constants constants;
	
	public static UIAttribute createAttribute()
	{
		UIAttribute attribute = new UIAttribute();
		attribute.setId(Document.get().createUniqueId());
		attribute.setName(new UIQName(constants.getDefaultNamespace(), constants.getDefaultAttributeName()));
		attribute.setType(new UIQName(constants.getDefaultNamespace(), constants.getDefaultAttributeType()));
		attribute.setLanguage("");
		attribute.setValue(constants.getDefaultAttributeValue());
		return attribute;
	}

}
