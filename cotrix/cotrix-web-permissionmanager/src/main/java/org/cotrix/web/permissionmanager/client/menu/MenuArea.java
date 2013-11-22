/**
 * 
 */
package org.cotrix.web.permissionmanager.client.menu;

import org.cotrix.web.permissionmanager.client.AdminArea;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuArea extends MenuItem {
	
	protected AdminArea adminArea;

	/**
	 * @param label
	 * @param adminArea
	 */
	public MenuArea(String label, AdminArea adminArea) {
		super(label);
		this.adminArea = adminArea;
	}

	/**
	 * @return the adminArea
	 */
	public AdminArea getAdminArea() {
		return adminArea;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MenuArea [adminArea=");
		builder.append(adminArea);
		builder.append(", label=");
		builder.append(label);
		builder.append("]");
		return builder.toString();
	}

}
