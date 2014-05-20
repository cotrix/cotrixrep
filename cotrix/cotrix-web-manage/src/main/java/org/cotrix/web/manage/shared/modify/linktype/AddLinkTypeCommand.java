/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.shared.modify.AddCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AddLinkTypeCommand extends AddCommand<UILinkType> implements LinkTypeCommand {

	protected AddLinkTypeCommand() {
	}

	public AddLinkTypeCommand(UILinkType item) {
		super(item);
	}
}
