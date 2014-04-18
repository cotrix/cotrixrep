/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.manage.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.shared.modify.GenericCommand.Action;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.code.CodeTargetedCommand;
import org.cotrix.web.manage.shared.modify.link.LinkCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeLinkCommandGenerator implements CommandGenerator<CodeLink> {
	
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
}
