/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.client.codelist.attribute.event.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.linktype.AddLinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.RemoveLinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.UpdateLinkTypeCommand;
import org.cotrix.web.manage.shared.modify.linktype.UpdatedLinkType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypeModifyGenerator implements CommandGenerator<UILinkType> {
	
	@Inject
	@EditorBus 
	private EventBus editorBus;
	
	@Override
	public Class<UILinkType> getType() {
		return UILinkType.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, UILinkType data) {
		switch (editType) {
			case ADD: {
				AddLinkTypeCommand addLinkTypeCommand = new AddLinkTypeCommand(data);
				return addLinkTypeCommand;
			}
			case UPDATE: {
				UpdateLinkTypeCommand updateLinkTypeCommand = new UpdateLinkTypeCommand(data);
				return updateLinkTypeCommand;
			}
			case REMOVE: {
				RemoveLinkTypeCommand removeLinkTypeCommand = new RemoveLinkTypeCommand(data.getId());
				return removeLinkTypeCommand;
			}
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
		
		editorBus.fireEvent(new AttributesUpdatedEvent(localLinkType));
	}

}
