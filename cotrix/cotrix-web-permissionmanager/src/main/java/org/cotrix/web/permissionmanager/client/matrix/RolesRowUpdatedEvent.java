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
	
	protected RolesRow row; 
	protected String role; 
	protected boolean value;
	
	/**
	 * @param row
	 * @param role
	 * @param value
	 */
	public RolesRowUpdatedEvent(RolesRow row, String role, boolean value) {
		this.row = row;
		this.role = role;
		this.value = value;
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
