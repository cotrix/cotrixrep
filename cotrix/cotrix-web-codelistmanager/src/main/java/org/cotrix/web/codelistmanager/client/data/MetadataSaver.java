/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.shared.CodelistMetadata;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.metadata.UpdateMetadataCommand;

import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataSaver extends AbstractDataSaver<CodelistMetadata> {

	/**
	 * @param codelistId
	 */
	@Inject
	public MetadataSaver(MetadataEditor editor) {
		editor.addDataEditHandler(this);
	}

	@Override
	public ModifyCommand generateCommand(EditType editType,	CodelistMetadata data) {
		switch (editType) {
			case UPDATE: return new UpdateMetadataCommand(data.getName());
			default: throw new UnsupportedOperationException("Metadata edit type "+editType+" not supported");
		}
		
	}

}
