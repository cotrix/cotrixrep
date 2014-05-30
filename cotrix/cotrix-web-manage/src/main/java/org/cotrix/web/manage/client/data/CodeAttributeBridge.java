/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.codelist.codes.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandBridge;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.modify.GenericCommand.Action;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.UpdatedCode;
import org.cotrix.web.manage.shared.modify.attribute.AttributeCommand;
import org.cotrix.web.manage.shared.modify.code.CodeTargetedCommand;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeBridge implements CommandBridge<CodeAttribute> {
	
	@Inject
	@CodelistBus 
	private EventBus codelistBus;
	
	@Override
	public Class<CodeAttribute> getType() {
		return CodeAttribute.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, CodeAttribute data) {
		switch (editType) {
			case ADD: {
				AttributeCommand addAttributeCommand = new AttributeCommand(Action.ADD, data.getAttribute());
				return new CodeTargetedCommand(data.getCode().getId(), addAttributeCommand);
			}
			case UPDATE: {
				AttributeCommand updateAttributeCommand = new AttributeCommand(Action.UPDATE, data.getAttribute());
				return new CodeTargetedCommand(data.getCode().getId(), updateAttributeCommand);
			}
			case REMOVE: {
				AttributeCommand removeAttributeCommand = new AttributeCommand(Action.REMOVE, data.getAttribute());
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
		
		codelistBus.fireEvent(new CodeUpdatedEvent(localCode));
	}
}
