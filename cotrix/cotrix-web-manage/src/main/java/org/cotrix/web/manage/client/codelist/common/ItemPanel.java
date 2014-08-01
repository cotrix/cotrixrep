/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common;

import org.cotrix.web.common.client.widgets.CustomDisclosurePanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanelListener;
import org.cotrix.web.manage.client.util.LabelHeader;
import org.cotrix.web.manage.client.util.LabelHeader.Button;
import org.cotrix.web.manage.client.util.LabelHeader.HeaderListener;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ItemPanel<T> extends Composite implements ItemEditingPanel<T> {

	public interface ItemEditor<T> extends HasValueChangeHandlers<Void> {
		public void startEditing();
		public void stopEditing();
		public void onEdit(AsyncCallback<Boolean> callBack);

		public void read();
		public void write();
		public String getLabel();
		public String getLabelValue();

		public boolean validate();

		public T getItem();

		public IsWidget getView();
		public boolean isSwitchVisible();
		public ImageResource getBullet();
	}

	private boolean readOnly;
	private boolean editable;
	private boolean editing;

	private LabelHeader header;
	private ItemEditingPanelListener<T> listener;
	private ItemEditor<T> editor;

	private CustomDisclosurePanel disclosurePanel;

	public ItemPanel(ItemEditor<T> editor) {
		this.editor = editor;

		header = new LabelHeader();
		header.setSwitchVisible(editor.isSwitchVisible());
		ImageResource bullet = editor.getBullet();
		if (bullet!=null) header.setBulletImage(bullet);

		header.setSaveTitle("Save all changes.");
		header.setRevertTitle("Discard all changes.");
		header.setEditTitle("Make changes.");

		disclosurePanel = new CustomDisclosurePanel(header);
		disclosurePanel.setWidth("100%");
		disclosurePanel.setAnimationEnabled(true);

		IsWidget detailsPanel = editor.getView();
		disclosurePanel.add(detailsPanel);
		initWidget(disclosurePanel);

		editor.addValueChangeHandler(new ValueChangeHandler<Void>() {

			@Override
			public void onValueChange(ValueChangeEvent<Void> event) {
				updateHeaderLabel();
				validate();
			}
		});

		disclosurePanel.addCloseHandler(new CloseHandler<CustomDisclosurePanel>() {

			@Override
			public void onClose(CloseEvent<CustomDisclosurePanel> event) {
				header.setEditVisible(false);
				header.setControlsVisible(false);
				fireSelected();
			}
		});

		disclosurePanel.addOpenHandler(new OpenHandler<CustomDisclosurePanel>() {

			@Override
			public void onOpen(OpenEvent<CustomDisclosurePanel> event) {
				updateHeaderButtons();
				fireSelected();
				if (editing) validate();
			}
		});

		header.setListener(new HeaderListener() {

			@Override
			public void onButtonClicked(Button button) {
				switch (button) {
					case EDIT: onEdit(); break;
					case REVERT: onCancel(); break;
					case SAVE: onSave(); break;
				}
			}

			@Override
			public void onSwitchChange(boolean isDown) {
				onSwitch(isDown);
			}
		});

		editor.stopEditing();
		editing = false;
		editable = false;

		writeItem();
		updateHeaderLabel();
	}

	public void setSwitchVisible(boolean visible) {
		header.setSwitchVisible(visible);
	}

	private void fireSelected() {
		if (listener!=null) listener.onSelect();
	}

	public void setSelected(boolean selected) {
		header.setHeaderSelected(selected);
	}

	private void onSave() {
		stopEdit();
		readItem();
		if (listener!=null) listener.onSave(editor.getItem());
		updateHeaderLabel();
	}

	private void onEdit() {
		editor.onEdit(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					startEdit();
					validate();
				}
			}
		});

	}

	private void onSwitch(boolean isDown) {
		if (listener!=null) listener.onSwitch(isDown);
	}

	public void syncWithModel() {
		writeItem();
	}

	private void readItem() {
		editor.read();
	}

	public void enterEditMode() {
		editable = true;
		editing = true;
		disclosurePanel.setOpen(true);
		startEdit();
	}

	private void startEdit() {
		editing = true;
		editor.startEditing();
		updateHeaderButtons();
		updateHeaderLabel();
	}

	private void stopEdit() {
		editing = false;
		editor.stopEditing();
		updateHeaderButtons();	
	}

	private void onCancel() {
		stopEdit();
		if (listener!=null) listener.onCancel();
		writeItem();
		updateHeaderLabel();
	}

	private void writeItem() {
		editor.write();
	}

	private void updateHeaderLabel() {
		header.setHeaderLabel(editor.getLabel());
		header.setHeaderLabelValue(editor.getLabelValue());
	}

	private void updateHeaderButtons() {
		if (disclosurePanel.isOpen()) {
			header.setEditVisible(!editing && editable && !readOnly);
			header.setControlsVisible(editing);
			header.setRevertVisible(editing);
			header.setSaveVisible(false);
		} else {
			header.setEditVisible(false);
			header.setControlsVisible(false);
			header.setRevertVisible(false);
			header.setSaveVisible(false);
		}
	}

	private void validate() {
		boolean valid = editor.validate();
		header.setSaveVisible(valid);
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		updateHeaderButtons();
	}

	@Override
	public void setListener(ItemEditingPanelListener<T> listener) {
		this.listener = listener;
	}

	@Override
	public void setSwitchDown(boolean down) {
		header.setSwitchDown(down);
	}

}
