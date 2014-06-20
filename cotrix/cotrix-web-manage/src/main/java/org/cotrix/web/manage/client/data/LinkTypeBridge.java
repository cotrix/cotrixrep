/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandBridge;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.shared.modify.GenericCommand.Action;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.linktype.LinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.UpdatedLinkType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypeBridge implements CommandBridge<UILinkType> {
	
	@Inject
	@CodelistBus 
	private EventBus codelistBus;
	
	@Override
	public Class<UILinkType> getType() {
		return UILinkType.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, UILinkType data) {
		switch (editType) {
			case ADD: return new LinkTypeCommand(Action.ADD, data);
			case UPDATE: return new LinkTypeCommand(Action.UPDATE, data);
			case REMOVE: return new LinkTypeCommand(Action.REMOVE, data);
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

	@Override
	public void handleResponse(EditType editType, UILinkType localLinkType, ModifyCommandResult response) {
		
		if (editType == EditType.REMOVE) return;
		
		UpdatedLinkType updatedLinkTypeResponse = (UpdatedLinkType) response;
		
		UILinkType updatedLinkType = updatedLinkTypeResponse.getUpdatedLinkType();
		
		//update the id
		localLinkType.setId(updatedLinkType.getId());
		
		//update the attributes
		localLinkType.setAttributes(updatedLinkType.getAttributes());
		
		codelistBus.fireEvent(new AttributesUpdatedEvent(localLinkType));
	}

}
