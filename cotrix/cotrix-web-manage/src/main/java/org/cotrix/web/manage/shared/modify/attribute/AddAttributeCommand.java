/**
 * 
 */
package org.cotrix.web.manage.shared.modify.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.shared.modify.AddCommand;

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
