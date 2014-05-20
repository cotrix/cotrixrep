/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.manage.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.attribute.AddAttributeCommand;
import org.cotrix.web.manage.shared.modify.attribute.RemoveAttributeCommand;
import org.cotrix.web.manage.shared.modify.attribute.UpdateAttributeCommand;
import org.cotrix.web.manage.shared.modify.code.CodeTargetedCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeCommandGenerator implements CommandGenerator<CodeAttribute> {
	
	@Override
	public Class<CodeAttribute> getType() {
		return CodeAttribute.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, CodeAttribute data) {
		switch (editType) {
			case ADD: {
				AddAttributeCommand addAttributeCommand = new AddAttributeCommand(data.getAttribute());
				return new CodeTargetedCommand(data.getCode().getId(), addAttributeCommand);
			}
			case UPDATE: {
				UpdateAttributeCommand updateAttributeCommand = new UpdateAttributeCommand(data.getAttribute());
				return new CodeTargetedCommand(data.getCode().getId(), updateAttributeCommand);
			}
			case REMOVE: {
				RemoveAttributeCommand removeAttributeCommand = new RemoveAttributeCommand(data.getAttribute().getId());
				return new CodeTargetedCommand(data.getCode().getId(), removeAttributeCommand);
			}
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}
}
