/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.AttributeGroup;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupFactory {
	
	public static AttributeGroup getGroup(UIAttribute attribute)
	{
		boolean isSystemGroup = Attributes.isSystemAttribute(attribute);
		return new AttributeGroup(attribute.getName(), null, attribute.getLanguage(), isSystemGroup);
	}

}
