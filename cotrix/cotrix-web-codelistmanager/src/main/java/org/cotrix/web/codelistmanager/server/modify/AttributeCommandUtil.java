/**
 * 
 */
package org.cotrix.web.codelistmanager.server.modify;

import org.cotrix.domain.common.Attribute;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AddAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.RemoveAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.UpdateAttributeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeCommandUtil {
	
	public static Attribute handle(AttributeCommand command)
	{
		Attribute attribute = null;
		if (command instanceof AddAttributeCommand) {
			AddAttributeCommand addAttributeCommand = (AddAttributeCommand)command;
			attribute = ChangesetUtil.addAttribute(addAttributeCommand.getItem());
		}

		if (command instanceof UpdateAttributeCommand) {
			UpdateAttributeCommand updateAttributeCommand = (UpdateAttributeCommand)command;
			attribute = ChangesetUtil.updateAttribute(updateAttributeCommand.getAttribute());
		}

		if (command instanceof RemoveAttributeCommand) {
			RemoveAttributeCommand removeAttributeCommand = (RemoveAttributeCommand)command;
			attribute = ChangesetUtil.removeAttribute(removeAttributeCommand.getId());
		}

		if (attribute == null) throw new IllegalArgumentException("Unknown attribute command "+command);
		return attribute;
	}

}
