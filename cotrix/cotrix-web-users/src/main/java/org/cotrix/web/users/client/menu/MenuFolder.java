/**
 * 
 */
package org.cotrix.web.users.client.menu;

import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MenuFolder extends MenuItem {
	
	protected List<MenuItem> children;

	/**
	 * @param label
	 * @param children
	 */
	public MenuFolder(String label, List<MenuItem> children) {
		super(label, "");
		this.children = children;
	}

	/**
	 * @return the children
	 */
	public List<MenuItem> getChildren() {
		return children;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MenuFolder [children=");
		builder.append(children);
		builder.append(", label=");
		builder.append(label);
		builder.append("]");
		return builder.toString();
	}

}
