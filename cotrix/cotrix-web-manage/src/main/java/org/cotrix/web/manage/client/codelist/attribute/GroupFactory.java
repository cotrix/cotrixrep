/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.Group;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupFactory {
	
	public static Group getGroup(UIAttribute attribute)
	{
		boolean isSystemGroup = Attributes.isSystemAttribute(attribute);
		return new Group(attribute.getName(), null, attribute.getLanguage(), isSystemGroup);
	}

}
