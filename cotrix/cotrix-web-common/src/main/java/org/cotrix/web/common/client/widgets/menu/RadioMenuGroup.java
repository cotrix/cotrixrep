/**
 * 
 */
package org.cotrix.web.common.client.widgets.menu;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RadioMenuGroup extends AbstractMenuGroup {
	
	@UiChild(tagname="radio")
	public void add(final CheckMenuItem item) {
		addItem(item);
		
		item.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				updateSelection(item);
			}
		});
	}
	
	private void updateSelection(CheckMenuItem selectedItem) {
		for (CheckMenuItem item:getItems()) item.setSelected(item == selectedItem);
	}
}
