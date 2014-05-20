/**
 * 
 */
package org.cotrix.web.manage.shared.modify.attribute;

import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.shared.modify.ModifyCommand;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdateAttributeCommand implements ModifyCommand, AttributeCommand {

	protected UIAttribute attribute;

	protected UpdateAttributeCommand(){}
	
	/**
	 * @param attribute
	 */
	public UpdateAttributeCommand(UIAttribute attribute) {
		this.attribute = attribute;
	}

	/**
	 * @return the attribute
	 */
	public UIAttribute getAttribute() {
		return attribute;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UpdateAttributeCommand [attribute=");
		builder.append(attribute);
		builder.append("]");
		return builder.toString();
	}
}
