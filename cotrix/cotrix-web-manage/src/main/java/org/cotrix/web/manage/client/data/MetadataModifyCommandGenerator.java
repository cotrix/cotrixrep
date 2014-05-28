/**
 * 
 */
package org.cotrix.web.manage.client.data;

import org.cotrix.web.common.shared.codelist.UICodelistMetadata;
import org.cotrix.web.manage.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.manage.client.data.event.EditType;
import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;
import org.cotrix.web.manage.shared.modify.metadata.UpdateMetadataCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataModifyCommandGenerator implements CommandGenerator<UICodelistMetadata> {

	@Override
	public Class<UICodelistMetadata> getType() {
		return UICodelistMetadata.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType,	UICodelistMetadata data) {
		switch (editType) {
			case UPDATE: return new UpdateMetadataCommand(data.getName());
			default: throw new UnsupportedOperationException("Metadata edit type "+editType+" not supported");
		}
	}

	@Override
	public void handleResponse(EditType editType, UICodelistMetadata data, ModifyCommandResult response) {
		//nothing to do
	}

}
