/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.shared.modify.GenericCommand.Action;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.attributetype.AttributeTypeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypeModifyGenerator implements CommandGenerator<UIAttributeType> {
	
	@Override
	public Class<UIAttributeType> getType() {
		return UIAttributeType.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType, UIAttributeType data) {
		switch (editType) {
			case ADD: return new AttributeTypeCommand(Action.ADD, data);
			case UPDATE: return new AttributeTypeCommand(Action.UPDATE, data);
			case REMOVE: return new AttributeTypeCommand(Action.REMOVE, data);
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

}
