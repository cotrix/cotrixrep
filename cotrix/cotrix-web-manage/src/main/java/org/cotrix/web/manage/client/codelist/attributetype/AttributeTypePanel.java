/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attributetype;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.CustomDisclosurePanel;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.common.shared.codelist.attributetype.UIConstraint;
import org.cotrix.web.common.shared.codelist.attributetype.UIRange;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanelListener;
import org.cotrix.web.manage.client.util.LabelHeader;
import org.cotrix.web.manage.client.util.LabelHeader.Button;
import org.cotrix.web.manage.client.util.LabelHeader.HeaderListener;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Composite;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypePanel extends Composite implements ItemEditingPanel<UIAttributeType> {

	private boolean readOnly;
	private boolean editable;
	private boolean editing;

	private LabelHeader header;
	private AttributeTypeDetailsPanel detailsPanel;
	private ItemEditingPanelListener<UIAttributeType> listener;
	private UIAttributeType attributeType;

	private CustomDisclosurePanel disclosurePanel;

	private String id = Document.get().createUniqueId();

	public AttributeTypePanel(UIAttributeType attribute) {
		this.attributeType = attribute;
		
		header = new LabelHeader();
		header.setSwitchVisible(false);
		disclosurePanel = new CustomDisclosurePanel(header);
		disclosurePanel.setWidth("100%");
		disclosurePanel.setAnimationEnabled(true);
		
		detailsPanel = new AttributeTypeDetailsPanel();
		disclosurePanel.add(detailsPanel);
		initWidget(disclosurePanel);

		detailsPanel.addValueChangeHandler(new ValueChangeHandler<Void>() {

			@Override
			public void onValueChange(ValueChangeEvent<Void> event) {
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

		detailsPanel.setReadOnly(true);
		editing = false;
		editable = false;
		
		writeLink();
		updateHeaderLabel();
	}
	
	public void setSwitchVisible(boolean visible) {
		header.setSwitchVisible(visible);
	}

	public String getId() {
		return id;
	}

	private void fireSelected() {
		if (listener!=null) listener.onSelect();
	}

	public void setSelected(boolean selected) {
		header.setHeaderSelected(selected);
	}

	private void onSave() {
		stopEdit();
		readAttributeType();
		if (listener!=null) listener.onSave(attributeType);
		updateHeaderLabel();
	}

	private void onEdit() {
		startEdit();
		validate();
	}
	
	private void onSwitch(boolean isDown) {
		if (listener!=null) listener.onSwitch(isDown);
	}
	
	public void syncWithModel() {
		writeLink();
	}

	private void readAttributeType() {
		String name = detailsPanel.getName();
		attributeType.setName(ValueUtils.getValue(name));
		
		String type = detailsPanel.getType();
		attributeType.setType(ValueUtils.getValue(type));
		
		Language language = detailsPanel.getLanguage();
		attributeType.setLanguage(language);
		
		String defaultValue = detailsPanel.getDefault();
		attributeType.setDefaultValue(defaultValue);
		
		UIRange range = detailsPanel.getRange();
		attributeType.setRange(range);
		
		List<UIConstraint> constraints = detailsPanel.getConstraints();
		attributeType.setConstraints(constraints);
	}

	public void enterEditMode() {
		editable = true;
		editing = true;
		disclosurePanel.setOpen(true);
		startEdit();
	}

	private void startEdit() {
		editing = true;
		detailsPanel.setReadOnly(false);
		updateHeaderButtons();
	}

	private void stopEdit() {
		editing = false;
		detailsPanel.setReadOnly(true);
		updateHeaderButtons();	
	}

	private void onCancel() {
		stopEdit();
		if (listener!=null) listener.onCancel();
		writeLink();
	}

	private void writeLink() {
		detailsPanel.setName(ValueUtils.getLocalPart(attributeType.getName()));
		detailsPanel.setType(ValueUtils.getLocalPart(attributeType.getType()));
		detailsPanel.setLanguage(attributeType.getLanguage());
		detailsPanel.setDefault(attributeType.getDefaultValue());
		detailsPanel.setRange(attributeType.getRange());
		detailsPanel.setConstraints(attributeType.getConstraints());
	}
	
	private void updateHeaderLabel() {
		header.setHeaderLabel(ValueUtils.getLocalPart(attributeType.getName()));
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
		boolean valid = true;

		String name = detailsPanel.getName();
		boolean nameValid = name!=null && !name.isEmpty();
		detailsPanel.setNameFieldValid(nameValid);
		valid &= nameValid;
		
		UIRange range = detailsPanel.getRange();
		boolean rangeValid = range.getMin()!=Integer.MIN_VALUE && range.getMax()!=Integer.MIN_VALUE;
		detailsPanel.setRangeFieldValid(rangeValid);
		valid &= rangeValid;
		
		header.setSaveVisible(valid);
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		updateHeaderButtons();
	}

	@Override
	public void setListener(ItemEditingPanelListener<UIAttributeType> listener) {
		this.listener = listener;
	}

	@Override
	public void setSwitchDown(boolean down) {
		header.setSwitchDown(down);
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

}
