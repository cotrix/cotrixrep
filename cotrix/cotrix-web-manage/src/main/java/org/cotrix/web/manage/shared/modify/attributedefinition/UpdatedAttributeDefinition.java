/**
 * 
 */
package org.cotrix.web.manage.shared.modify.attributedefinition;

import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedAttributeDefinition extends ModifyCommandResult {
	
	private UIAttributeDefinition updatedAttribute;
	
	public UpdatedAttributeDefinition(){}

	public UpdatedAttributeDefinition(UIAttributeDefinition updatedAttribute) {
		this.updatedAttribute = updatedAttribute;
	}

	public UIAttributeDefinition getUpdatedAttribute() {
		return updatedAttribute;
	}
	
}
