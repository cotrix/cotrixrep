/**
 * 
 */
package org.cotrix.web.codelistmanager.client.codelist.attribute;

import org.cotrix.web.codelistmanager.client.util.Attributes;
import org.cotrix.web.codelistmanager.shared.Group;
import org.cotrix.web.share.shared.codelist.UIAttribute;
import org.cotrix.web.share.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupFactory {
	
	public static final AttributeField[] GROUP_MASK = new AttributeField[]{AttributeField.NAME, AttributeField.LANGUAGE};

	public static Group getGroup(UIAttribute attribute, AttributeField ... attributeFields)
	{
		UIQName name = contains(AttributeField.NAME, attributeFields)?attribute.getName():null;
		UIQName type = contains(AttributeField.TYPE, attributeFields)?attribute.getType():null;
		String language = contains(AttributeField.LANGUAGE, attributeFields)?attribute.getLanguage():null;
		boolean isSystemGroup = Attributes.isSystemAttribute(attribute);
		Group group = new Group(name, type, language, isSystemGroup);
		return group;
	}
	
	protected static boolean contains(AttributeField field, AttributeField[] fields)
	{
		if (fields == null || fields.length == 0) return false;
		for (AttributeField attributeField:fields) if (attributeField == field) return true;
		return false;
	}

}
