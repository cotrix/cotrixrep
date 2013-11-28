/**
 * 
 */
package org.cotrix.web.permissionmanager.client.menu;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class MenuItem {
	
	protected String label;

	/**
	 * @param label
	 */
	public MenuItem(String label) {
		this.label = label;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
}
