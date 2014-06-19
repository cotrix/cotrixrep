/**
 * 
 */
package org.cotrix.web.common.client.widgets.menu;

import java.util.List;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItemSeparator;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FlatMenuBar extends MenuBar {
	
	public interface Group {
		List<CheckMenuItem> getItems();
	}
	
	private boolean isFirstElement = true;
	private String itemSeparatorStyleName;

	@UiConstructor
	public FlatMenuBar(boolean vertical) {
		super(vertical);
	}
	
	public void setItemSeparatorStyleName(String itemSeparatorStyleName) {
		this.itemSeparatorStyleName = itemSeparatorStyleName;
	}

	@UiChild(tagname="group")
	public void addGroup(final Group group) {
		if (!isFirstElement) addSeparator();
		isFirstElement = false;
		for (CheckMenuItem item:group.getItems()) addItem(item);
	}
	
	@SuppressWarnings("deprecation")
	public MenuItemSeparator addSeparator() {
		MenuItemSeparator separator = new MenuItemSeparator();
		if (itemSeparatorStyleName!=null) setStyleName(DOM.getChild(separator.getElement(), 0), itemSeparatorStyleName);
		addSeparator(separator);
		return separator;
	}
	
}
