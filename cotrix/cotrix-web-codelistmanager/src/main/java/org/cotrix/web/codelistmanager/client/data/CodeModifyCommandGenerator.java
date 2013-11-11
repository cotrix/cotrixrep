/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.AddCodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.RemoveCodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.UpdateCodeCommand;
import org.cotrix.web.share.shared.codelist.UICode;

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
