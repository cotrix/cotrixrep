/**
 * 
 */
package org.cotrix.web.manage.shared.modify.metadata;

import org.cotrix.web.manage.shared.modify.ModifyCommand;
import org.cotrix.web.manage.shared.modify.attribute.AttributeCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MetadataAttributeCommand implements ModifyCommand {
	
	protected AttributeCommand command;
	
	protected MetadataAttributeCommand(){}
	
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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetadataAttributeCommand [command=");
		builder.append(command);
		builder.append("]");
		return builder.toString();
	}

}
