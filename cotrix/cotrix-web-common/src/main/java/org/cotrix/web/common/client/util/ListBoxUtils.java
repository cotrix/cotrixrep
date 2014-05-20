/**
 * 
 */
package org.cotrix.web.common.client.util;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ListBoxUtils {

	public static void selecteItem(ListBox listBox, String itemValue) {
		for (int i = 0; i<listBox.getItemCount(); i++) {
			if (listBox.getValue(i).equals(itemValue)) {
				listBox.setSelectedIndex(i);
				return;
			}
		}
		throw new IllegalArgumentException("Unknown itemValue "+itemValue);
	}

	public static void setItemColor(ListBox listBox, String itemValue, String color) {
		NodeList<Node> children = listBox.getElement().getChildNodes();       
		for (int i = 0; i< children.getLength();i++) {
			Node child = children.getItem(i);
			if (child.getNodeType()==Node.ELEMENT_NODE) {
				if (child instanceof OptionElement) {
					OptionElement optionElement = (OptionElement) child;
					if (optionElement.getValue().equals(itemValue)) {
						optionElement.getStyle().setColor(color);  
					}                   
				}
			}           
		}
	}

}
