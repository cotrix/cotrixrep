/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import org.cotrix.web.manage.shared.modify.RemoveCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RemoveLinkTypeCommand extends RemoveCommand implements LinkTypeCommand {

	/**
	 * 
	 */
	protected RemoveLinkTypeCommand() {
	}

	/**
	 * @param id
	 */
	public RemoveLinkTypeCommand(String id) {
		super(id);
	}
}
