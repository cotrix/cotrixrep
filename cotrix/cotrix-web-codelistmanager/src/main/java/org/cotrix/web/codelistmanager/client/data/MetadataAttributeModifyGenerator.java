/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AddAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.RemoveAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.UpdateAttributeCommand;
import org.cotrix.web.codelistmanager.shared.modify.metadata.MetadataAttributeCommand;
import org.cotrix.web.share.shared.codelist.UIAttribute;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataAttributeModifyGenerator implements CommandGenerator<UIAttribute> {
	
	@Override
	public Class<UIAttribute> getType() {
		return UIAttribute.class;
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
