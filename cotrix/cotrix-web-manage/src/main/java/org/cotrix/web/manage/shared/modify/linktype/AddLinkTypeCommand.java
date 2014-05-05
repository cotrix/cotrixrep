/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import org.cotrix.web.common.shared.codelist.HasAttributes;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.shared.modify.AddCommand;
import org.cotrix.web.manage.shared.modify.ContainsAttributed;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AddLinkTypeCommand extends AddCommand<UILinkType> implements LinkTypeCommand, ContainsAttributed {

	/**
	 * 
	 */
	protected AddLinkTypeCommand() {
	}

	/**
	 * @param item
	 */
	public AddLinkTypeCommand(UILinkType item) {
		super(item);
	}

	@Override
	public HasAttributes getAttributed() {
		return item;
	}
}
