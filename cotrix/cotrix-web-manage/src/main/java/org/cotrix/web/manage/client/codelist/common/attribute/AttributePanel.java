/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.CustomDisclosurePanel;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanel;
import org.cotrix.web.manage.client.codelist.common.ItemsEditingPanel.ItemEditingPanelListener;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;
import org.cotrix.web.manage.client.util.LabelHeader;
import org.cotrix.web.manage.client.util.LabelHeader.Button;
import org.cotrix.web.manage.client.util.LabelHeader.HeaderListener;

import com.allen_sauer.gwt.log.client.Log;
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
public class AttributePanel extends Composite implements ItemEditingPanel<UIAttribute> {

	private boolean readOnly;
	private boolean editable;
	private boolean editing;

	private LabelHeader header;
	private AttributeDetailsPanel detailsPanel;
	private ItemEditingPanelListener<UIAttribute> listener;
	private UIAttribute attribute;

	private CustomDisclosurePanel disclosurePanel;

	private String id = Document.get().createUniqueId();

	public AttributePanel(UIAttribute attribute, boolean hasDefinition, AttributeDescriptionSuggestOracle oracle, AttributeDefinitionsCache attributeTypesCache) {
		this.attribute = attribute;
		
		header = new LabelHeader();
		header.setSwitchVisible(true);
		disclosurePanel = new CustomDisclosurePanel(header);
		disclosurePanel.setWidth("100%");
		disclosurePanel.setAnimationEnabled(true);
		
		if (Attributes.isSystemAttribute(attribute)) header.addHeaderStyle(CotrixManagerResources.INSTANCE.css().systemAttributeDisclosurePanelLabel());

		detailsPanel = new AttributeDetailsPanel(oracle, attributeTypesCache);
		detailsPanel.setDefinitionVisible(hasDefinition);
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
				updateHeaderLabel();
				fireSelected();
			}
		});

		disclosurePanel.addOpenHandler(new OpenHandler<CustomDisclosurePanel>() {

			@Override
			public void onOpen(OpenEvent<CustomDisclosurePanel> event) {
				updateHeaderButtons();
				updateHeaderLabel();
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
		
		writeType();
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
		readAttribute();
		if (listener!=null) listener.onSave(attribute);
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
		writeType();
	}

	private void readAttribute() {
		
		UIAttributeDefinition definition = detailsPanel.getDefinition();
		attribute.setDefinitionId(definition==null?null:definition.getId());
		
		UIQName name = detailsPanel.getName();
		attribute.setName(name);
		
		String type = detailsPanel.getType();
		String typeNamespace = ValueUtils.defaultNamespace;
		UIQName oldType = attribute.getType();
		//we preserve the namespace
		if (oldType!=null && oldType.getLocalPart().equals(type)) typeNamespace = oldType.getNamespace();
		attribute.setType(new UIQName(typeNamespace, type));
		
		String description = detailsPanel.getDescription();
		attribute.setDescription(description);
		
		Language language = detailsPanel.getLanguage();
		attribute.setLanguage(language);
		
		String value = detailsPanel.getValue();
		attribute.setValue(value);

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
		writeType();
	}

	private void writeType() {
		detailsPanel.setDefinitionId(attribute.getDefinitionId());
		detailsPanel.setName(attribute.getName());
		detailsPanel.setType(ValueUtils.getLocalPart(attribute.getType()));
		detailsPanel.setDescription(attribute.getDescription());
		detailsPanel.setLanguage(attribute.getLanguage());
		detailsPanel.setValue(attribute.getValue());
	}
	
	private void updateHeaderLabel() {
		header.setHeaderLabel(ValueUtils.getLocalPart(attribute.getName()));
		String valueLabel = attribute.getValue()!=null?attribute.getValue():"n/a";
		header.setHeaderLabelValue(": "+valueLabel);
		header.setHeaderValueVisible(!disclosurePanel.isOpen() && attribute.getValue()!=null);
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

		Log.trace("validating");
		
		UIQName name = detailsPanel.getName();
		Log.trace("name: "+name);
		
		boolean nameValid = name!=null && !name.isEmpty();
		Log.trace("nameValid: "+nameValid);
		detailsPanel.setNameFieldValid(nameValid);
		valid &= nameValid;
		
		UIAttributeDefinition definition = detailsPanel.getDefinition();
		if (definition!=null) {
			boolean valueValid = evaluate(definition.getExpression(), detailsPanel.getValue());
			detailsPanel.setValueFieldValid(valueValid, "Be warned, the current value does not satisfy all the schema constraints for "+name.getLocalPart().toUpperCase());
		}

		header.setSaveVisible(valid);
	}
	
	private boolean evaluate(String expression, String value) {
		String filledExpression = expression.replaceAll("\\$value", "\""+value+"\"");
		Log.trace("evaluating "+filledExpression);
		return eval(filledExpression);
	}
	
	private native boolean eval(String expression) /*-{
		return $wnd.eval(expression);
	}-*/;

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
		updateHeaderButtons();
	}

	@Override
	public void setListener(ItemEditingPanelListener<UIAttribute> listener) {
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
