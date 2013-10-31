/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.DataSaverManager.CommandGenerator;
import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.metadata.UpdateMetadataCommand;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataModifyCommandGenerator implements CommandGenerator<CodelistMetadata> {

	@Override
	public Class<CodelistMetadata> getType() {
		return CodelistMetadata.class;
	}

	@Override
	public ModifyCommand generateCommand(EditType editType,	CodelistMetadata data) {
		switch (editType) {
			case UPDATE: return new UpdateMetadataCommand(data.getName());
			default: throw new UnsupportedOperationException("Metadata edit type "+editType+" not supported");
		}
		
	}

}
