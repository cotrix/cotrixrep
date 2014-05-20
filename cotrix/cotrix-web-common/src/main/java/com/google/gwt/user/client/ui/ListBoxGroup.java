/**
 * 
 */
package com.google.gwt.user.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.i18n.client.HasDirection.Direction;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ListBoxGroup {

	private OptGroupElement element;
	private GroupableListBox parent;

	protected ListBoxGroup(OptGroupElement element, GroupableListBox parent) {
		this.element = element;
		this.parent = parent;
	}

	/**
	 * Adds an item to the list box, specifying an initial value for the item.
	 * 
	 * @param item the text of the item to be added
	 * @param value the item's value, to be submitted if it is part of a
	 *          {@link FormPanel}; cannot be <code>null</code>
	 */
	public void addItem(String item, String value) {
		addItem(item, null, value);
	}

	/**
	 * Adds an item to the list box, specifying its direction and an initial value
	 * for the item.
	 * 
	 * @param item the text of the item to be added
	 * @param dir the item's direction
	 * @param value the item's value, to be submitted if it is part of a
	 *          {@link FormPanel}; cannot be <code>null</code>
	 */
	public void addItem(String item, Direction dir, String value) {
		OptionElement option = Document.get().createOptionElement();
		parent.setOptionText(option, item, dir);
		option.setValue(value);
		element.appendChild(option);
	}
}
