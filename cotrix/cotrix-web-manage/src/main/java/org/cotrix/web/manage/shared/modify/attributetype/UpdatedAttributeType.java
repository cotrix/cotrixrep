/**
 * 
 */
package org.cotrix.web.manage.shared.modify.attributetype;

import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.manage.shared.modify.ModifyCommandResult;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UpdatedAttributeType extends ModifyCommandResult {
	
	private UIAttributeType updatedAttribute;
	
	public UpdatedAttributeType(){}

	public UpdatedAttributeType(UIAttributeType updatedAttribute) {
		this.updatedAttribute = updatedAttribute;
	}

	public UIAttributeType getUpdatedAttribute() {
		return updatedAttribute;
	}
	
}
