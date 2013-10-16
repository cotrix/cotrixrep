/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify;

import org.cotrix.web.codelistmanager.shared.modify.attribute.AttributeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataAttributeCommand implements ModifyCommand {
	
	protected AttributeCommand command;
	
	/**
	 * @param command
	 */
	public MetadataAttributeCommand(AttributeCommand command) {
		this.command = command;
	}

	/**
	 * @return the command
	 */
	public AttributeCommand getCommand() {
		return command;
	}

}
