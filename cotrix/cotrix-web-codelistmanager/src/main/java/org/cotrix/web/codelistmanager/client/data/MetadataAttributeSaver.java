/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.modify.MetadataAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AddAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.RemoveAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.UpdateAttributeCommand;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataAttributeSaver extends AbstractDataSaver<UIAttribute> {

	/**
	 * @param codelistId
	 */
	@Inject
	public MetadataAttributeSaver(MetadataAttributeEditor editor) {
		editor.addDataEditHandler(this);
	}


	@Override
	public ModifyCommand generateCommand(EditType editType, UIAttribute data) {
		switch (editType) {
			case ADD: {
				AddAttributeCommand addAttributeCommand = new AddAttributeCommand(data);
				return new MetadataAttributeCommand(addAttributeCommand);
			}
			case UPDATE: {
				UpdateAttributeCommand updateAttributeCommand = new UpdateAttributeCommand(data);
				return new MetadataAttributeCommand(updateAttributeCommand);
			}
			case REMOVE: {
				RemoveAttributeCommand removeAttributeCommand = new RemoveAttributeCommand(data.getId());
				return new MetadataAttributeCommand(removeAttributeCommand);
			}
		}
		throw new IllegalArgumentException("Unknown edit type "+editType);
	}

}
