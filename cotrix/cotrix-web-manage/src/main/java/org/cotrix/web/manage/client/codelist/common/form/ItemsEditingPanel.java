/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.form;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.util.InstanceMap;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.manage.client.codelist.common.form.ItemsEditingPanel.ItemsEditingListener.SwitchState;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionChangeEvent.HasSelectionChangedHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ItemsEditingPanel<T> extends Composite implements HasEditing, HasSelectionChangedHandlers {

	public interface ItemsEditingListener<T> {
		public static enum SwitchState {DOWN, UP};
		
		public void onCreate(T item);
		public void onUpdate(T item);
		public void onSwitch(T item, SwitchState state);
	}
	
	public interface ItemEditingPanelListener<T> {

		public void onSave(T item);

		public void onCancel();

		public void onSelect();
		
		public void onSwitch(boolean isDown);
	}
		
	private VerticalPanel mainPanel;
	private HTML emptyWidget;
	private HorizontalPanel emptyWidgetContainer;
	private List<ItemPanel<T>> panels = new ArrayList<ItemPanel<T>>();

	private ItemPanel<T> currentSelection;

	private InstanceMap<T, ItemPanel<T>> instances = new InstanceMap<T, ItemPanel<T>>();

	private ItemsEditingListener<T> listener;

	private boolean editable;
	
	private ItemPanelFactory<T> editingPanelFactory;

	public ItemsEditingPanel(String noItemsText, ItemPanelFactory<T> editingPanelFactory) {
		this.editingPanelFactory = editingPanelFactory;
		
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		
		emptyWidget = new HTML(noItemsText);
		emptyWidget.setStyleName(CotrixManagerResources.INSTANCE.css().noItemsLabel());
		emptyWidgetContainer = new HorizontalPanel();
		emptyWidgetContainer.setWidth("100%");
		emptyWidgetContainer.setHeight("200px");
		emptyWidgetContainer.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		emptyWidgetContainer.add(emptyWidget);
		
		mainPanel.add(emptyWidgetContainer);
		
		initWidget(mainPanel);

		editable = false;
		updateEmptyWidget();
	}
	
	public void setNoItemsText(SafeHtml noItemsText) {
		emptyWidget.setHTML(noItemsText);
	}
	
	public void synchWithModel(T item) {
		ItemPanel<T> panel = instances.get(item);
		panel.syncWithModel();
	}
	
	public void setSwitchState(T item, SwitchState state) {
		ItemPanel<T> panel = instances.get(item);
		panel.setSwitchDown(state == SwitchState.DOWN);
	}

	public void setListener(ItemsEditingListener<T> listener) {
		this.listener = listener;
	}

	public void removeItem(T item) {
		ItemPanel<T> panel = instances.remove(item);
		if (panel == null) return;
		remove(panel);
		
		updateEmptyWidget();
	}

	public void clear() {
		for (ItemPanel<T> panel:panels) mainPanel.remove(panel);
		instances.clear();
		
		updateSelection(null);
		
		updateEmptyWidget();
	}

	public void addItemPanel(final T item) {
		final ItemPanel<T> panel = editingPanelFactory.createPanel(item);
		
		panel.setEditable(editable);
		panels.add(panel);

		instances.put(item, panel);

		panel.setListener(new ItemEditingPanelListener<T>() {

			@Override
			public void onSave(T item) {
				fireUpdate(item);
			}

			@Override
			public void onCancel() {
			}

			@Override
			public void onSelect() {
				updateSelection(panel);				
			}
			
			@Override
			public void onSwitch(boolean isDown) {
				fireSwitch(item, isDown);
			}
		});
		mainPanel.add(panel);
		
		updateEmptyWidget();
	}

	public void addNewItemPanel(final T item) {
		final ItemPanel<T> panel = editingPanelFactory.createPanel(item);
		
		instances.put(item, panel);
		
		panels.add(panel);
		
		panel.setListener(new ItemEditingPanelListener<T>() {

			boolean created = false;

			@Override
			public void onSave(T item) {
				if (!created) {
					fireCreate(item);
					created = true;
				} else fireUpdate(item);
			}

			@Override
			public void onCancel() {
				if (!created) {
					remove(panel);
					updateEmptyWidget();
				}
			}

			@Override
			public void onSelect() {
				updateSelection(panel);
			}
			
			@Override
			public void onSwitch(boolean isDown) {
				fireSwitch(item, isDown);
			}
		});
		mainPanel.add(panel);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				panel.enterEditMode();
			}
		});
		
		updateEmptyWidget();
	}
	
	private void updateEmptyWidget() {
		boolean showEmptyWidget = panels.isEmpty();
		emptyWidgetContainer.setVisible(showEmptyWidget);
		//setStyleName(CotrixManagerResources.INSTANCE.css().noItemsBackground(), showEmptyWidget);
	}
	
	private void remove(ItemPanel<T> toRemove) {
		mainPanel.remove(toRemove);
		panels.remove(toRemove);
		if (toRemove == currentSelection) updateSelection(null);
	}

	private void updateSelection(ItemPanel<T> selected) {
		if (currentSelection!=null) currentSelection.setSelected(false);
		if (selected!=null) selected.setSelected(true);
		currentSelection = selected;
		SelectionChangeEvent.fire(this);
	}

	public T getSelectedItem() {
		if (currentSelection!=null) return instances.getByValue(currentSelection);
		return null;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		for (ItemPanel<T> panel:panels) panel.setEditable(editable);
	}

	private void fireUpdate(T item) {
		if (listener!=null) listener.onUpdate(item);
	}

	private void fireCreate(T item) {
		if (listener!=null) listener.onCreate(item);
	}
	
	private void fireSwitch(T item, boolean isDown) {
		if (listener!=null) listener.onSwitch(item, isDown?SwitchState.DOWN:SwitchState.UP);
	}

	@Override
	public HandlerRegistration addSelectionChangeHandler(Handler handler) {
		return addHandler(handler, SelectionChangeEvent.getType());
	}
}
