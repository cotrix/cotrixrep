/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.manage.shared.modify.AddCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AddLinkTypeCommand extends AddCommand<UILinkType> implements LinkTypeCommand {

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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddLinkTypeCommand [item=");
		builder.append(item);
		builder.append("]");
		return builder.toString();
	}

}
