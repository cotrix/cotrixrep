/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.code;

import org.cotrix.web.codelistmanager.shared.UICode;
import org.cotrix.web.codelistmanager.shared.modify.AddCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AddCodeCommand extends AddCommand<UICode> implements CodeCommand {

	/**
	 * 
	 */
	protected AddCodeCommand() {
	}

	/**
	 * @param item
	 */
	public AddCodeCommand(UICode item) {
		super(item);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddCodeCommand [item=");
		builder.append(item);
		builder.append("]");
		return builder.toString();
	}
}
