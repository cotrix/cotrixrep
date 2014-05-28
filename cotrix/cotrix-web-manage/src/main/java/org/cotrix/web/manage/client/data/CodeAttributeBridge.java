/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.codelist.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandBridge;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.UpdatedCode;
import org.cotrix.web.manage.shared.modify.attribute.AddAttributeCommand;
import org.cotrix.web.manage.shared.modify.attribute.RemoveAttributeCommand;
import org.cotrix.web.manage.shared.modify.attribute.UpdateAttributeCommand;
import org.cotrix.web.manage.shared.modify.code.CodeTargetedCommand;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeBridge implements CommandBridge<CodeAttribute> {
	
	@Inject
	@EditorBus 
	private EventBus editorBus;
	
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

	@Override
	public void handleResponse(EditType editType, CodeAttribute localCodeAttribute, ModifyCommandResult response) {
		
		if (editType == EditType.REMOVE) return;
		
		UpdatedCode updatedCodeResponse = (UpdatedCode)response;
		
		//update the id
		UIAttribute localAttribute = localCodeAttribute.getAttribute();
		localAttribute.setId(updatedCodeResponse.getId());
		
		UICode localCode = localCodeAttribute.getCode();
		UICode updatedCode = updatedCodeResponse.getCode();
		//merge the system attributes
		Attributes.mergeSystemAttributes(localCode.getAttributes(), updatedCode.getAttributes());
		
		editorBus.fireEvent(new CodeUpdatedEvent(localCode));
	}
}
