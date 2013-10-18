/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AddAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.RemoveAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.UpdateAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.code.CodeAttributeCommand;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeSaver extends AbstractDataSaver<CodeAttributeEditor.CodeAttribute> {

	/**
	 * @param codelistId
	 */
	@Inject
	public CodeAttributeSaver(CodeAttributeEditor editor) {
		editor.addDataEditHandler(this);
	}


	@Override
	public ModifyCommand generateCommand(EditType editType, CodeAttributeEditor.CodeAttribute data) {
		switch (editType) {
			case ADD: {
				AddAttributeCommand addAttributeCommand = new AddAttributeCommand(data.getAttribute());
				return new CodeAttributeCommand(data.getCodeId(), addAttributeCommand);
			}
			case UPDATE: {
				UpdateAttributeCommand updateAttributeCommand = new UpdateAttributeCommand(data.getAttribute());
				return new CodeAttributeCommand(data.getCodeId(), updateAttributeCommand);
			}
			case REMOVE: {
				RemoveAttributeCommand removeAttributeCommand = new RemoveAttributeCommand(data.getAttribute().getId());
				return new CodeAttributeCommand(data.getCodeId(), removeAttributeCommand);
			}
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

}
