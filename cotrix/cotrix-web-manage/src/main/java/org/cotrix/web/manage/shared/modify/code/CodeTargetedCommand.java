/**
 * 
 */
package org.cotrix.web.manage.shared.modify.code;

import org.cotrix.web.manage.shared.modify.ModifyCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodeTargetedCommand implements ModifyCommand {
	
	protected String codeId;
	protected ModifyCommand command;
	
	protected CodeTargetedCommand(){}
	
	/**
	 * @param codeId
	 * @param command
	 */
	public CodeTargetedCommand(String codeId, ModifyCommand command) {
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
	public ModifyCommand getCommand() {
		return command;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CodeTargetedCommand [codeId=");
		builder.append(codeId);
		builder.append(", command=");
		builder.append(command);
		builder.append("]");
		return builder.toString();
	}

}
