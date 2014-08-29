/**
 * 
 */
package org.cotrix.web.common.client.widgets.menu;

import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RadioMenuGroup extends AbstractMenuGroup {
	
	private AbstractMenuItem selectedItem;

	@UiChild(tagname="radio")
	public void add(final AbstractMenuItem item) {
		addItem(item);
		if (item instanceof HasSelectionChangedHandlers) {
			((HasSelectionChangedHandlers)item).addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					selectItem(item);
				}
			});
		}
	}

	public void selectItem(AbstractMenuItem selectedItem) {
		this.selectedItem = selectedItem;
		for (AbstractMenuItem item:getItems()) item.setSelected(item == selectedItem);
	}
	
	public AbstractMenuItem getSelectedItem() {
		return selectedItem;
	}
}
