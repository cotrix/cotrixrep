/**
 * 
 */
package org.cotrix.web.permissionmanager.client.menu;

import org.cotrix.web.permissionmanager.client.AdminArea;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuSelectedEvent extends GenericEvent {
	
	protected AdminArea adminArea;

	/**
	 * @param adminArea
	 */
	public MenuSelectedEvent(AdminArea adminArea) {
		this.adminArea = adminArea;
	}

	/**
	 * @return the adminArea
	 */
	public AdminArea getAdminArea() {
		return adminArea;
	}

}
