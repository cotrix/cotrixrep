/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.attribute;

import org.cotrix.web.codelistmanager.shared.modify.RemoveCommand;

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

}
