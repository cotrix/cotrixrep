/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandBridge;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.shared.modify.GenericCommand.Action;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.linkdefinition.LinkDefinitionCommand;
import org.cotrix.web.manage.shared.modify.linkdefinition.UpdatedLinkDefinition;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDefinitionBridge implements CommandBridge<UILinkDefinition> {
	
	@Inject
	@CodelistBus 
	private EventBus codelistBus;
	
	@Override
	public Class<UILinkDefinition> getType() {
		return UILinkDefinition.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, UILinkDefinition data) {
		switch (editType) {
			case ADD: return new LinkDefinitionCommand(Action.ADD, data);
			case UPDATE: return new LinkDefinitionCommand(Action.UPDATE, data);
			case REMOVE: return new LinkDefinitionCommand(Action.REMOVE, data);
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

	@Override
	public void handleResponse(EditType editType, UILinkDefinition localLinkDefinition, ModifyCommandResult response) {
		
		if (editType == EditType.REMOVE) return;
		
		UpdatedLinkDefinition updatedLinkTypeResponse = (UpdatedLinkDefinition) response;
		
		UILinkDefinition updatedLinkDefinition = updatedLinkTypeResponse.getUpdatedLinkType();
		
		//update the id
		localLinkDefinition.setId(updatedLinkDefinition.getId());
		
		//update the attributes
		localLinkDefinition.setAttributes(updatedLinkDefinition.getAttributes());
		
		codelistBus.fireEvent(new AttributesUpdatedEvent(localLinkDefinition));
	}

}
