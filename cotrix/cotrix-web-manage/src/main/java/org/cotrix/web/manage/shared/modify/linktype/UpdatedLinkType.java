/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linktype;

import org.cotrix.web.common.shared.codelist.linktype.UILinkType;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedLinkType extends ModifyCommandResult {
	
	protected UILinkType updatedLinkType;
	
	protected UpdatedLinkType(){}

	public UpdatedLinkType(UILinkType link) {
		this.updatedLinkType = link;
	}

	public UILinkType getUpdatedLinkType() {
		return updatedLinkType;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdatedLinkType [updatedLinkType=");
		builder.append(updatedLinkType);
		builder.append("]");
		return builder.toString();
	}
}
