/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.attribute;

import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.modify.AddCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AddAttributeCommand extends AddCommand<UIAttribute> implements AttributeCommand {

	/**
	 * 
	 */
	protected AddAttributeCommand() {
	}

	/**
	 * @param item
	 */
	public AddAttributeCommand(UIAttribute item) {
		super(item);
	}
}
