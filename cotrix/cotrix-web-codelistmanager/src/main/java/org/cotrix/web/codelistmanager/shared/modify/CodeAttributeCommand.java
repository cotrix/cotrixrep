/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify;

import org.cotrix.web.codelistmanager.shared.modify.attribute.AttributeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeCommand implements ModifyCommand {
	
	protected String codeId;
	protected AttributeCommand command;
	
	/**
	 * @param codeId
	 * @param command
	 */
	public CodeAttributeCommand(String codeId, AttributeCommand command) {
		this.codeId = codeId;
		this.command = command;
	}

	/**
	 * @return the id
	 */
	public String getCodeId() {
		return codeId;
	}

	/**
	 * @return the command
	 */
	public AttributeCommand getCommand() {
		return command;
	}

}
