/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.table.AbstractRow;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeRow extends AbstractRow {
	
	private static final String HEADER_STYLE = CotrixManagerResources.INSTANCE.propertyGrid().header();
	private static final String TEXTBOX_STYLE = CommonResources.INSTANCE.css().textBox()+ " " + CotrixManagerResources.INSTANCE.css().editor();
	private static final String TEXTVALUE_STYLE = CotrixManagerResources.INSTANCE.propertyGrid().textValue();
	private static final int NAME_COL = 0;
	private static final int VALUE_COL = 1;
	private static final int DELETE_COL = 2;
	private static final int FULL_EDIT_COL = 3;
	
	private AttributeRowListener listener;
	private ValueChangeHandler<String> changeHandler;
	
	private TextBox nameTextBox;
	private EditableLabel nameEditableLabel;
	
	private TextBox valueTextBox;
	private EditableLabel valueEditableLabel;
	
	private PushButton deleteButton;
	private PushButton fullEditButton;
	
	private boolean readOnly = false;
	
	private String errorStyle;
	
	public AttributeRow(String errorStyle) {
		
		this.errorStyle = errorStyle;
		
		changeHandler = new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fireValueChanged();
			}
		};
		
		nameTextBox = new TextBox();
		nameTextBox.setHeight("31px");
		nameTextBox.setStyleName(TEXTBOX_STYLE);
		
		nameEditableLabel = new EditableLabel();
		nameEditableLabel.addEditor(nameTextBox);
		nameEditableLabel.setLabelStyle(TEXTVALUE_STYLE);
		nameEditableLabel.setReadOnly(readOnly);
		
		nameTextBox.addValueChangeHandler(nameEditableLabel);
		nameTextBox.addValueChangeHandler(changeHandler);
		
		valueTextBox = new TextBox();
		valueTextBox.setHeight("31px");
		valueTextBox.setStyleName(TEXTBOX_STYLE);
		valueTextBox.addValueChangeHandler(changeHandler);
		
		valueEditableLabel = new EditableLabel();
		valueEditableLabel.addEditor(valueTextBox);
		valueEditableLabel.setLabelStyle(TEXTVALUE_STYLE);
		valueEditableLabel.setReadOnly(readOnly);
		
		valueTextBox.addValueChangeHandler(valueEditableLabel);
		
		deleteButton = new PushButton(new Image(CommonResources.INSTANCE.minus()));
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireButtonClicked(Button.DELETE);
			}
		});
		
		fullEditButton = new PushButton(new Image(CotrixManagerResources.INSTANCE.edit()));
		fullEditButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireButtonClicked(Button.FULL_EDIT);
			}
		});
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(AttributeRowListener listener) {
		this.listener = listener;
	}

	@Override
	public void setup() {
			
		addCell(NAME_COL, nameEditableLabel);
		setCellStyle(NAME_COL, CotrixManagerResources.INSTANCE.propertyGrid().value());
		table.getCellFormatter().getElement(rowIndex, NAME_COL).getStyle().setBackgroundColor("#efefef");

		addCell(VALUE_COL, valueEditableLabel);
		setCellStyle(VALUE_COL, CotrixManagerResources.INSTANCE.propertyGrid().value());
		
		addCell(DELETE_COL, deleteButton);
		setCellStyle(DELETE_COL, CotrixManagerResources.INSTANCE.propertyGrid().value());
		
		addCell(FULL_EDIT_COL, fullEditButton);
		setCellStyle(FULL_EDIT_COL, CotrixManagerResources.INSTANCE.propertyGrid().value());		
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		nameEditableLabel.setReadOnly(readOnly);
		valueEditableLabel.setReadOnly(readOnly);
		updateValueCellSpan();
	}
	
	public int getRowIndex() {
		Position position = getCellPosition(nameEditableLabel);
		return position.getRow();
	}
	
	private void updateValueCellSpan() {
		int span = readOnly?3:1;
		table.getFlexCellFormatter().setColSpan(getRowIndex(), VALUE_COL, span);
		table.getCellFormatter().setVisible(getRowIndex(), DELETE_COL, !readOnly);
		table.getCellFormatter().setVisible(getRowIndex(), FULL_EDIT_COL, !readOnly);
	}
	
	public String getName() {
		return nameTextBox.getValue();
	}
	
	public void setName(String name) {
		nameTextBox.setValue(name);
		nameEditableLabel.setText(name);
	}
	
	public String getValue() {
		return valueTextBox.getValue();
	}
	
	public void setValue(String value) {
		valueTextBox.setValue(value);
		valueEditableLabel.setText(value);
	}
	
	private void fireValueChanged() {
		if (listener!=null) listener.onValueChanged(this);
	}
	
	private void fireButtonClicked(Button button) {
		if (listener!=null) listener.onButtonClicked(this, button);
	}
	
	public void setValid(boolean valid) {
		nameTextBox.setStyleName(errorStyle, !valid);
		/*Element rowElement = table.getRowFormatter().getElement(getRowIndex());
		if (valid) rowElement.addClassName(errorStyle);
		else rowElement.removeClassName(errorStyle);*/
	}
	
	public enum Button {DELETE, FULL_EDIT;}
	
	public interface AttributeRowListener {
		public void onButtonClicked(AttributeRow row, Button button);
		public void onValueChanged(AttributeRow row);
	}
}