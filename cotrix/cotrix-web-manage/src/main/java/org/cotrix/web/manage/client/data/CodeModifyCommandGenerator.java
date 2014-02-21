/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.code.AddCodeCommand;
import org.cotrix.web.manage.shared.modify.code.RemoveCodeCommand;
import org.cotrix.web.manage.shared.modify.code.UpdateCodeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeModifyCommandGenerator implements CommandGenerator<UICode> {
	
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

}
