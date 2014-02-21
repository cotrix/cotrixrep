/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist.attribute;

import org.cotrix.web.codelistmanager.client.util.Attributes;
import org.cotrix.web.codelistmanager.shared.Group;
import org.cotrix.web.share.shared.codelist.UIAttribute;

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
