/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.code;

import org.cotrix.web.codelistmanager.shared.modify.RemoveCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RemoveCodeCommand extends RemoveCommand implements CodeCommand  {

	/**
	 * 
	 */
	protected RemoveCodeCommand() {
	}

	/**
	 * @param id
	 */
	public RemoveCodeCommand(String id) {
		super(id);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RemoveCodeCommand [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
