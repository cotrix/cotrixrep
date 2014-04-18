/**
 * 
 */
package org.cotrix.web.manage.shared.modify.code;

import org.cotrix.web.manage.shared.modify.RemoveCommand;

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
}
