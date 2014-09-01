/**
 * 
 */
package org.cotrix.web.common.client.widgets.menu;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.widgets.menu.FlatMenuBar.Group;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractMenuGroup implements Group, HasSelectionHandlers<AbstractMenuItem> {
	
	private HandlerManager handlerManager = new HandlerManager(this);
	private List<AbstractMenuItem> items = new ArrayList<AbstractMenuItem>();
	
	protected void addItem(final AbstractMenuItem item) {
		items.add(item);
		if (item instanceof HasSelectionChangedHandlers) {
			((HasSelectionChangedHandlers)item).addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				
				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					fireSelection(item);
				}
			});
		}
	}
	
	public List<AbstractMenuItem> getItems() {
		return items;
	}

	private void fireSelection(AbstractMenuItem item) {
		SelectionEvent.fire(this, item);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<AbstractMenuItem> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	
	public List<AbstractMenuItem> getSelectedItems() {
		List<AbstractMenuItem> selected = new ArrayList<AbstractMenuItem>();
		for (AbstractMenuItem item:items) if (item.isSelected()) selected.add(item);
		return selected;
	}

}
