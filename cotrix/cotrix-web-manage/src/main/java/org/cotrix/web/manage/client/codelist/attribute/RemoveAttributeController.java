/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attribute;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RemoveAttributeController {
	
	private boolean userCanEdit = false;
	private boolean attributeCanBeRemoved = false;
	
	public void setUserCanEdit(boolean userCanEdit) {
		this.userCanEdit = userCanEdit;
	}

	public void setAttributeCanBeRemoved(boolean attributeCanBeRemoved) {
		this.attributeCanBeRemoved = attributeCanBeRemoved;
	}

	public boolean canRemove() {
		return userCanEdit && attributeCanBeRemoved;
	}
}
