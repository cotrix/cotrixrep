/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.UICode;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.AddCodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.RemoveCodeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.UpdateCodeCommand;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeSaver extends AbstractDataSaver<UICode> {

	/**
	 * @param codelistId
	 */
	@Inject
	public CodeSaver(CodeEditor editor) {
		editor.addDataEditHandler(this);
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
