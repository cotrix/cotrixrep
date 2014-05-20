/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.shared.modify.ModifyCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdateLinkTypeCommand implements ModifyCommand, LinkTypeCommand {

	protected UILinkType linktype;

	protected UpdateLinkTypeCommand(){}
	
	/**
	 * @param attribute
	 */
	public UpdateLinkTypeCommand(UILinkType linktype) {
		this.linktype = linktype;
	}

	/**
	 * @return the linktype
	 */
	public UILinkType getLinkType() {
		return linktype;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateLinkTypeCommand [linktype=");
		builder.append(linktype);
		builder.append("]");
		return builder.toString();
	}

}
