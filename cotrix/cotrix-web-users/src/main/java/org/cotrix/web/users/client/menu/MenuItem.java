/**
 * 
 */
package org.cotrix.web.users.client.menu;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class MenuItem {
	
	protected String label;
	protected String imageHtml;

	/**
	 * @param label
	 */
	public MenuItem(String label, String imageHtml) {
		this.label = label;
		this.imageHtml = imageHtml;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the imageHtml
	 */
	public String getImageHtml() {
		return imageHtml;
	}
	
}
