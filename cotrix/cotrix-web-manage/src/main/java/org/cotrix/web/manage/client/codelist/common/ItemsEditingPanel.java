/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.util.InstanceMap;
import org.cotrix.web.common.client.widgets.HasEditing;
import org.cotrix.web.manage.client.codelist.attribute.AttributesGridResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ItemsEditingPanel<T,P extends ItemsEditingPanel.ItemEditingPanel<T>> extends Composite implements HasEditing {

	public interface ItemsEditingListener<T> {
		public void onCreate(T item);
		public void onUpdate(T item);
	}
	
	public interface ItemEditingPanel<T> extends IsWidget, HasEditing {
		public void syncWithModel();
		public void enterEditMode();
		public void setSelected(boolean selected);
		public void setListener(ItemEditingPanelListener<T> listener);
	}
	
	public interface ItemEditingPanelListener<T> {

		public void onSave(T item);

		public void onCancel();

		public void onSelect();
	}
		
	private VerticalPanel mainPanel;
	private List<P> panels = new ArrayList<P>();

	private final static AttributesGridResources gridResource = GWT.create(AttributesGridResources.class);
	private P currentSelection;

	private InstanceMap<T, P> instances = new InstanceMap<T, P>();

	private ItemsEditingListener<T> listener;

	private boolean editable;

	public ItemsEditingPanel(String headerText) {
		mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");

		gridResource.dataGridStyle().ensureInjected();

		Label header = new Label(headerText);
		header.setStyleName(gridResource.dataGridStyle().dataGridHeader());
		mainPanel.add(header);

		initWidget(mainPanel);

		editable = false;
	}
	
	public void synchWithModel(T item) {
		P panel = instances.get(item);
		panel.syncWithModel();
	}

	public void setListener(ItemsEditingListener<T> listener) {
		this.listener = listener;
	}

	public void removeItem(T item) {
		P panel = instances.remove(item);
		if (panel == null) return;
		if (currentSelection == panel) currentSelection = null;
		mainPanel.remove(panel);
		panels.remove(panel);
	}

	public void clear() {
		for (P panel:panels) mainPanel.remove(panel);
		instances.clear();
	}

	public void addItemPanel(final P panel, T item) {
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
		});
		mainPanel.add(panel);
	}

	public void addNewItemPanel(final P panel, T item) {
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
				if (!created) mainPanel.remove(panel);
			}

			@Override
			public void onSelect() {
				updateSelection(panel);
			}
		});
		mainPanel.add(panel);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				panel.enterEditMode();
			}
		});
	}

	private void updateSelection(P selected) {
		if (currentSelection!=null) currentSelection.setSelected(false);
		selected.setSelected(true);
		currentSelection = selected;
	}

	public T getSelectedItem() {
		if (currentSelection!=null) return instances.getByValue(currentSelection);
		return null;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		for (P panel:panels) panel.setEditable(editable);
	}

	private void fireUpdate(T item) {
		if (listener!=null) listener.onUpdate(item);
	}

	private void fireCreate(T item) {
		if (listener!=null) listener.onCreate(item);
	}
}
