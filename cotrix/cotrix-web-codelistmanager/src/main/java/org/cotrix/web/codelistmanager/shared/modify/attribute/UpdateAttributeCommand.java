/**
 * 
 */
package org.cotrix.web.codelistmanager.shared.modify.attribute;

import org.cotrix.web.codelistmanager.shared.UIAttribute;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;

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
}
