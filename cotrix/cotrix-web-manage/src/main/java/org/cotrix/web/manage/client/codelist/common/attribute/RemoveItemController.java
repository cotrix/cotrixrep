/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RemoveItemController {
	
	private boolean userCanEdit = false;
	private boolean itemCanBeRemoved = false;
	
	public void setUserCanEdit(boolean userCanEdit) {
		this.userCanEdit = userCanEdit;
	}

	public void setItemCanBeRemoved(boolean itemCanBeRemoved) {
		this.itemCanBeRemoved = itemCanBeRemoved;
	}

	public boolean canRemove() {
		return userCanEdit && itemCanBeRemoved;
	}
}
