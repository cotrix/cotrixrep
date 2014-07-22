/**
 * 
 */
package org.cotrix.web.manage.shared.modify.linkdefinition;

import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedLinkDefinition extends ModifyCommandResult {
	
	protected UILinkDefinition updatedLinkType;
	
	protected UpdatedLinkDefinition(){}

	public UpdatedLinkDefinition(UILinkDefinition link) {
		this.updatedLinkType = link;
	}

	public UILinkDefinition getUpdatedLinkType() {
		return updatedLinkType;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdatedLinkDefinition [updatedLinkType=");
		builder.append(updatedLinkType);
		builder.append("]");
		return builder.toString();
	}
}
