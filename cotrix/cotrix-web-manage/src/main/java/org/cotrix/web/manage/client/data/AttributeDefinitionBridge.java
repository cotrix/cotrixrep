/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandBridge;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.shared.modify.GenericCommand.Action;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.attributedefinition.AttributeDefinitionCommand;
import org.cotrix.web.manage.shared.modify.attributedefinition.UpdatedAttributeDefinition;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionBridge implements CommandBridge<UIAttributeDefinition> {
	
	@Override
	public Class<UIAttributeDefinition> getType() {
		return UIAttributeDefinition.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, UIAttributeDefinition data) {
		switch (editType) {
			case ADD: return new AttributeDefinitionCommand(Action.ADD, data);
			case UPDATE: return new AttributeDefinitionCommand(Action.UPDATE, data);
			case REMOVE: return new AttributeDefinitionCommand(Action.REMOVE, data);
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

	@Override
	public void handleResponse(EditType editType, UIAttributeDefinition localAttributeDefinition, ModifyCommandResult response) {
		
		if (editType == EditType.REMOVE) return;
		
		UpdatedAttributeDefinition updatedAttributeDefinitionResponse = (UpdatedAttributeDefinition)response;
		UIAttributeDefinition updatedAttributeType = updatedAttributeDefinitionResponse.getUpdatedAttribute();
		
		//set the new id
		localAttributeDefinition.setId(updatedAttributeType.getId());
		
		//set the expression
		localAttributeDefinition.setExpression(updatedAttributeType.getExpression());
	}

}
