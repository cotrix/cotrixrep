/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AddAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.RemoveAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.UpdateAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.CodeAttributeCommand;

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
				return new CodeAttributeCommand(data.getCode().getId(), addAttributeCommand);
			}
			case UPDATE: {
				UpdateAttributeCommand updateAttributeCommand = new UpdateAttributeCommand(data.getAttribute());
				return new CodeAttributeCommand(data.getCode().getId(), updateAttributeCommand);
			}
			case REMOVE: {
				RemoveAttributeCommand removeAttributeCommand = new RemoveAttributeCommand(data.getAttribute().getId());
				return new CodeAttributeCommand(data.getCode().getId(), removeAttributeCommand);
			}
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}
}
