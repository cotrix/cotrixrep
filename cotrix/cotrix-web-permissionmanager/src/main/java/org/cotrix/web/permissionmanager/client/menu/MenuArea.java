/**
 * 
 */
package org.cotrix.web.permissionmanager.client.menu;

import org.cotrix.web.permissionmanager.client.AdminArea;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

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
	public MenuArea(String label, AdminArea adminArea, ImageResource image) {
		super(label, AbstractImagePrototype.create(image).getHTML());
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
