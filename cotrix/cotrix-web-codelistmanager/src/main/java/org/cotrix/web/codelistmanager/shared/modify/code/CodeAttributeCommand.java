/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.code;

import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.codelistmanager.shared.modify.attribute.AttributeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeAttributeCommand implements ModifyCommand {
	
	protected String codeId;
	protected AttributeCommand command;
	
	protected CodeAttributeCommand(){}
	
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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodeAttributeCommand [codeId=");
		builder.append(codeId);
		builder.append(", command=");
		builder.append(command);
		builder.append("]");
		return builder.toString();
	}

}
