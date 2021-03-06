/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandBridge;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.shared.modify.GeneratedId;
import org.cotrix.web.manage.shared.modify.GenericCommand.Action;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.attribute.AttributeCommand;
import org.cotrix.web.manage.shared.modify.metadata.MetadataAttributeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataAttributeBridge implements CommandBridge<UIAttribute> {
	
	@Override
	public Class<UIAttribute> getType() {
		return UIAttribute.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, UIAttribute data) {
		switch (editType) {
			case ADD: {
				AttributeCommand addAttributeCommand = new AttributeCommand(Action.ADD, data);
				return new MetadataAttributeCommand(addAttributeCommand);
			}
			case UPDATE: {
				AttributeCommand updateAttributeCommand = new AttributeCommand(Action.UPDATE, data);
				return new MetadataAttributeCommand(updateAttributeCommand);
			}
			case REMOVE: {
				AttributeCommand removeAttributeCommand = new AttributeCommand(Action.REMOVE, data);
				return new MetadataAttributeCommand(removeAttributeCommand);
			}
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

	@Override
	public void handleResponse(EditType editType, UIAttribute localAttribute, ModifyCommandResult response) {
		if (editType == EditType.REMOVE) return;
		
		GeneratedId generatedIdResponse = (GeneratedId)response;
		//updates the id
		localAttribute.setId(generatedIdResponse.getId());
	}

}
