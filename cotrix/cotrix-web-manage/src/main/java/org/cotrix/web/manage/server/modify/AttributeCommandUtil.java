/**
 * 
 */
package org.cotrix.web.manage.server.modify;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.shared.modify.attribute.AttributeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeCommandUtil {
	
	public static Attribute handle(AttributeCommand command)
	{
		UIAttribute item = command.getItem();
		Attribute attribute = null;
		
		switch (command.getAction()) {
			case ADD: attribute = ChangesetUtil.addAttribute(item); break;
			case UPDATE: attribute = ChangesetUtil.updateAttribute(item); break;
			case REMOVE: attribute = ChangesetUtil.removeAttribute(item.getId()); break;
		}

		if (attribute == null) throw new IllegalArgumentException("Unknown attribute command "+command);
		return attribute;
	}

}
