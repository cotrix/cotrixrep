/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UILink;
import org.cotrix.web.manage.client.codelist.attribute.event.AttributesUpdatedEvent;
import org.cotrix.web.manage.client.codelist.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.codelist.link.ValueUpdatedEvent;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandHandler;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.event.EditorBus;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.modify.GenericCommand.Action;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.code.CodeTargetedCommand;
import org.cotrix.web.manage.shared.modify.link.LinkCommand;
import org.cotrix.web.manage.shared.modify.link.UpdatedLink;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeLinkHandler implements CommandHandler<CodeLink> {
	
	@Inject
	@EditorBus 
	private EventBus editorBus;
	
	@Override
	public Class<CodeLink> getType() {
		return CodeLink.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, CodeLink data) {
		switch (editType) {
			case ADD: {
				LinkCommand command = new LinkCommand(Action.ADD, data.getLink());
				return new CodeTargetedCommand(data.getCode().getId(), command);
			}
			case UPDATE: {
				LinkCommand command = new LinkCommand(Action.UPDATE, data.getLink());
				return new CodeTargetedCommand(data.getCode().getId(), command);
			}
			case REMOVE: {
				LinkCommand command = new LinkCommand(Action.REMOVE, data.getLink());
				return new CodeTargetedCommand(data.getCode().getId(), command);
			}
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

	@Override
	public void handleResponse(EditType editType, CodeLink localCodeLink, ModifyCommandResult response) {
		
		if (editType == EditType.REMOVE) return;
		
		UpdatedLink updateLinkResponse = (UpdatedLink)response;
		
		UILink localLink = localCodeLink.getLink();
		UICode localCode = localCodeLink.getCode();
		
		UILink updatedLink = updateLinkResponse.getUpdatedLink();
		UICode updatedCode = updateLinkResponse.getCode();
		
		//update the id
		localLink.setId(updatedLink.getId());
		//updates the attributes
		localLink.setAttributes(updatedLink.getAttributes());
		editorBus.fireEvent(new AttributesUpdatedEvent(localLink));
		//updates the value
		localLink.setValue(updatedLink.getValue());
		editorBus.fireEvent(new ValueUpdatedEvent(localLink));
		
		//merge the system attributes
		Attributes.mergeSystemAttributes(localCode.getAttributes(), updatedCode.getAttributes());
		
		editorBus.fireEvent(new CodeUpdatedEvent(localCode));
		
	}
}
