/**
 * 
 */
package com.google.gwt.user.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptGroupElement;
import com.google.gwt.dom.client.SelectElement;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GroupableListBox extends ListBox {


	public ListBoxGroup addGroup(String name) {
		SelectElement element = getSelectElement();
		OptGroupElement groupElement = Document.get().createOptGroupElement();
		groupElement.setLabel(name);
		element.appendChild(groupElement);
		return new ListBoxGroup(groupElement, this);
	}


	private SelectElement getSelectElement() {
		return getElement().cast();
	}
}
