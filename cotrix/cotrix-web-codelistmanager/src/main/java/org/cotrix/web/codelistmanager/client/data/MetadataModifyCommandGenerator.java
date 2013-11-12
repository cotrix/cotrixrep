/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.metadata.UpdateMetadataCommand;
import org.cotrix.web.share.shared.codelist.UICodelistMetadata;

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

}
