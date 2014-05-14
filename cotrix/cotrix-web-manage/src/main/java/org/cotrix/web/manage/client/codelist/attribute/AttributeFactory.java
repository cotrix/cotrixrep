/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attribute;

import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.manage.client.util.Constants;

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
		attribute.setLanguage(Language.NONE);
		attribute.setValue(constants.getDefaultAttributeValue());
		return attribute;
	}

}
