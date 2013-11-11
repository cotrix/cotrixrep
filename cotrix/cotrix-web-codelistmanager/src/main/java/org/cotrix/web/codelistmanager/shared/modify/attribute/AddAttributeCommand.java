/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.attribute;

import org.cotrix.web.codelistmanager.shared.modify.AddCommand;
import org.cotrix.web.share.shared.codelist.UIAttribute;

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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AddAttributeCommand [item=");
		builder.append(item);
		builder.append("]");
		return builder.toString();
	}

}
