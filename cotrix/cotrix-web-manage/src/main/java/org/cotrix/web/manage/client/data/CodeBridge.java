/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.codelist.codes.event.CodeUpdatedEvent;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandBridge;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.client.di.CodelistBus;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.UpdatedCode;
import org.cotrix.web.manage.shared.modify.code.AddCodeCommand;
import org.cotrix.web.manage.shared.modify.code.RemoveCodeCommand;
import org.cotrix.web.manage.shared.modify.code.UpdateCodeCommand;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeBridge implements CommandBridge<UICode> {
	
	@Inject
	@CodelistBus 
	private EventBus codelistBus;
	
	@Override
	public Class<UICode> getType() {
		return UICode.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, UICode data) {
		switch (editType) {
			case ADD: {
				return new AddCodeCommand(data);
			}
			case UPDATE: {
				return new UpdateCodeCommand(data.getId(), data.getName());
			}
			case REMOVE: {
				return new RemoveCodeCommand(data.getId());
			}
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

	@Override
	public void handleResponse(EditType editType, UICode localCode, ModifyCommandResult response) {
		if (!(response instanceof UpdatedCode)) throw new IllegalArgumentException("Unexpected response type "+response);
		
		if (editType == EditType.REMOVE) return;
		
		UpdatedCode updatedCodeResponse = (UpdatedCode)response;
		
		UICode updatedCode = updatedCodeResponse.getCode();
		
		//updates the id
		localCode.setId(updatedCode.getId());
		
		//merge the system attributes
		Attributes.mergeSystemAttributes(localCode.getAttributes(), updatedCode.getAttributes());
		
		codelistBus.fireEvent(new CodeUpdatedEvent(localCode));
	}

}
