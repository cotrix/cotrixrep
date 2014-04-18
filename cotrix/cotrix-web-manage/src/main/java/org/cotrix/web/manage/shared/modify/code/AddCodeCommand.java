/**
 * 
 */
package org.cotrix.web.manage.shared.modify.code;

import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.manage.shared.modify.AddCommand;

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
}
