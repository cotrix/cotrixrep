/**
 * 
 */
package org.cotrix.web.manage.shared.modify.attribute;

import org.cotrix.web.manage.shared.modify.RemoveCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RemoveAttributeCommand extends RemoveCommand implements AttributeCommand {

	/**
	 * 
	 */
	protected RemoveAttributeCommand() {
	}

	/**
	 * @param id
	 */
	public RemoveAttributeCommand(String id) {
		super(id);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RemoveAttributeCommand [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
