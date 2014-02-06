/**
 * 
 */
package org.cotrix.web.permissionmanager.client.matrix;

import org.cotrix.web.permissionmanager.shared.RolesRow;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RolesRowUpdatedEvent extends GenericEvent {
	
	protected int rowIndex;
	protected RolesRow row; 
	protected String role; 
	protected boolean value;
	
	/**
	 * @param row
	 * @param role
	 * @param value
	 */
	public RolesRowUpdatedEvent(int rowIndex, RolesRow row, String role, boolean value) {
		this.rowIndex = rowIndex;
		this.row = row;
		this.role = role;
		this.value = value;
	}

	/**
	 * @return the rowIndex
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * @return the row
	 */
	public RolesRow getRow() {
		return row;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @return the value
	 */
	public boolean getValue() {
		return value;
	}

}
