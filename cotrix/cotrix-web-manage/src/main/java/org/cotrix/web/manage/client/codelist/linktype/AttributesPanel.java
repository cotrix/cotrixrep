/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.table.AbstractRow;
import org.cotrix.web.common.client.widgets.table.Table;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.manage.client.codelist.linktype.AttributeEditDialog.AttributeEditDialogListener;
import org.cotrix.web.manage.client.codelist.linktype.AttributeRow.AttributeRowListener;
import org.cotrix.web.manage.client.codelist.linktype.AttributeRow.Button;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributesPanel implements HasValueChangeHandlers<Void> {
	
	private Table table;
	
	private AddRow addRow;
	private List<AttributeRow> rows;
	private Map<AttributeRow, UIAttribute> attributes;
	
	private AttributeRowListener rowListener;
	
	private boolean readOnly = true;
	
	private AttributeEditDialog attributeEditDialog;
	private UIAttribute currentEditedAttribute;
	private AttributeRow currentEditedRow;
	
	private String errorStyle;
	
	private boolean valid;
	
	private HandlerManager handlerManager;
	
	public AttributesPanel(Table table, String errorStyle) {
		this.table = table;
		this.errorStyle = errorStyle;
		
		addAddRow();
		setAddRowReadOnly(readOnly);

		rows = new ArrayList<AttributeRow>();
		rowListener = new AttributeRowListener() {
			
			@Override
			public void onValueChanged(AttributeRow row) {
				UIAttribute attribute = attributes.get(row);
				attribute.setName(ValueUtils.getValue(row.getName()));
				attribute.setValue(row.getValue());
				fireValueChanged();
			}
			
			@Override
			public void onButtonClicked(AttributeRow row, Button button) {
				switch (button) {
					case DELETE: removeRow(row); break;
					case FULL_EDIT: fullEdit(row); break;
				}
			}
		};
		
		attributes = new HashMap<AttributeRow, UIAttribute>();
		
		attributeEditDialog = new AttributeEditDialogImpl();
		attributeEditDialog.setListener(new AttributeEditDialogListener() {
			
			@Override
			public void onEdit(String name, String type, String language, String value) {
				currentEditedAttribute.setName(ValueUtils.getValue(name));
				currentEditedAttribute.setType(ValueUtils.getValue(type));
				currentEditedAttribute.setLanguage(language);
				currentEditedAttribute.setValue(value);
				
				currentEditedRow.setName(name);
				currentEditedRow.setValue(value);
				
				attributeEditDialog.hide();
				fireValueChanged();
				
			}
		});
		
		handlerManager = new HandlerManager(this);
	}
	
	private void addAddRow() {
		addRow = new AddRow();
		addRow.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addEmptyAttributeRow();				
			}
		});
		table.addRow(addRow);		
	}
	
	private void addEmptyAttributeRow() {
		AttributeRow attributeRow = new AttributeRow(errorStyle);
		attributes.put(attributeRow, new UIAttribute());
		addAttributeRow(attributeRow);
		fireValueChanged();
	}
	
	private void removeRow(AttributeRow row) {
		deattachRow(row);
		rows.remove(row);
		attributes.remove(row);
		fireValueChanged();
	}
	
	private void deattachRow(AttributeRow row) {
		table.getFlexTable().removeRow(row.getRowIndex());
		row.setListener(null);
	}
	
	private void fullEdit(AttributeRow row) {
		currentEditedRow = row;
		currentEditedAttribute = attributes.get(row);
		String name = ValueUtils.getValue(currentEditedAttribute.getName());
		String type = ValueUtils.getValue(currentEditedAttribute.getType());
		String language = currentEditedAttribute.getLanguage();
		String value = currentEditedAttribute.getValue();
		
		attributeEditDialog.set(name, type, language, value);
		attributeEditDialog.showCentered();
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		setAddRowReadOnly(readOnly);
		for (Entry<AttributeRow, UIAttribute> entry:attributes.entrySet()) entry.getKey().setReadOnly(Attributes.isSystemAttribute(entry.getValue()) || readOnly);
		if (!readOnly) validate();
	}
	
	private void setAddRowReadOnly(boolean readOnly) {
		table.getFlexTable().getRowFormatter().setVisible(addRow.getRow(), !readOnly);
	}
	
	public void setAttributes(List<UIAttribute> attributes) {
		removeAllRows();
		for (UIAttribute attribute:attributes) addAttribute(attribute);
		validate();
	}
	
	private void removeAllRows() {
		for (AttributeRow row:rows) deattachRow(row);
		rows.clear();
		attributes.clear();
		fireValueChanged();
	}
	
	private void addAttribute(UIAttribute attribute) {
		AttributeRow attributeRow = new AttributeRow(errorStyle);
		attributeRow.setName(ValueUtils.getLocalPart(attribute.getName()));
		attributeRow.setValue(attribute.getValue());
		addAttributeRow(attributeRow);
		attributeRow.setReadOnly(Attributes.isSystemAttribute(attribute) || readOnly);
		
		attributes.put(attributeRow, attribute);
	}
	
	private void addAttributeRow(AttributeRow attributeRow) {
		attributeRow.setListener(rowListener);
		rows.add(attributeRow);
		table.insertRow(addRow.getRow(), attributeRow);
		attributeRow.setReadOnly(readOnly);
	}
	
	public List<UIAttribute> getAttributes() {
		return new ArrayList<UIAttribute>(attributes.values());
	}
	
	private void fireValueChanged() {
		validate();
		ValueChangeEvent.fire(this, null);
	}
	
	public void validate() {
		Log.trace("validating rows");
		valid = true;
		for (Entry<AttributeRow, UIAttribute> entry:attributes.entrySet()) valid &= validate(entry.getKey(), entry.getValue());
	}
	
	private boolean validate(AttributeRow row, UIAttribute attribute) {
		boolean valid = row.getName() != null && !row.getName().isEmpty();
		row.setValid(valid);
		return valid;
	}

	public boolean areValid() {
		return valid;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return handlerManager.addHandler(ValueChangeEvent.getType(), handler);
	}

	private class AddRow extends AbstractRow implements HasClickHandlers {
		
		private com.google.gwt.user.client.ui.Button button;
		
		public AddRow() {
			button = new com.google.gwt.user.client.ui.Button();
			button.setStyleName(CotrixManagerResources.INSTANCE.css().addButton());
		}

		@Override
		public void setup() {
			addCell(0, button);
			table.getFlexCellFormatter().setColSpan(rowIndex, 0, 4);
		}
		
		public int getRow() {
			return getCellPosition(button).getRow();
		}

		@Override
		public void fireEvent(GwtEvent<?> event) {			
		}

		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return button.addClickHandler(handler);
		}
		
	}
}
