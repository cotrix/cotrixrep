/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.metadata;

import org.cotrix.web.codelistmanager.shared.UIQName;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdateMetadataCommand implements ModifyCommand, MetadataCommand {
	
	protected UIQName name;
	
	protected UpdateMetadataCommand(){}

	/**
	 * @param name
	 */
	public UpdateMetadataCommand(UIQName name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}

}
