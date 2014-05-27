/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attributetype.constraint;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.EditableLabel;
import org.cotrix.web.common.client.widgets.table.AbstractRow;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.resources.CotrixManagerResources.AttributeRowStyle;
import org.cotrix.web.manage.client.resources.CotrixManagerResources.PropertyGridStyle;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ConstraintRow extends AbstractRow {
	
	private static final PropertyGridStyle propertyGridStyles = CotrixManagerResources.INSTANCE.propertyGrid();
	private static final String LIST_STYLE = CommonResources.INSTANCE.css().listBox();
	private static final String TEXTVALUE_STYLE = propertyGridStyles.textValue();
	private static final AttributeRowStyle ATTRIBUTE_ROW_STYLE = CotrixManagerResources.INSTANCE.attributeRow();

	private static final int LABEL_COL = 0;
	private static final int VALUE_COL = 1;
	private static final int DELETE_COL = 2;
	private static final int FULL_EDIT_COL = 3;
	
	private List<ConstraintRowListener> listeners = new ArrayList<ConstraintRowListener>();
	
	private Label label;
	
	private ListBox constraintNameListBox;
	private EditableLabel constraintNameEditableLabel;
	
	private PushButton deleteButton;
	//private PushButton fullEditButton;
	
	private boolean readOnly = false;
	
	private String errorStyle;
	
	private MetaConstraintProvider metaConstraintProvider = GWT.create(MetaConstraintProvider.class);
	
	public ConstraintRow(String errorStyle) {
		
		CotrixManagerResources.INSTANCE.attributeRow().ensureInjected();
		
		this.errorStyle = errorStyle;
		
		label = new Label("Constraint");
				
		constraintNameListBox = new ListBox(false);
		constraintNameListBox.setStyleName(LIST_STYLE);
		
		for (MetaConstraint metaConstraint:metaConstraintProvider.getMetaConstraints()) constraintNameListBox.addItem(metaConstraint.getName());
		
		constraintNameEditableLabel = new EditableLabel();
		constraintNameEditableLabel.addEditor(constraintNameListBox);
		constraintNameEditableLabel.setLabelStyle(TEXTVALUE_STYLE);
		constraintNameEditableLabel.setReadOnly(readOnly);
		
		constraintNameListBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				constraintNameEditableLabel.setText(constraintNameListBox.getItemText(constraintNameListBox.getSelectedIndex()));
				fireValueChanged();
			}
		});
		
		deleteButton = new PushButton(new Image(CommonResources.INSTANCE.minus()));
		deleteButton.setStyleName(ATTRIBUTE_ROW_STYLE.button());
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireButtonClicked(Button.DELETE);
			}
		});
		
		/*fullEditButton = new PushButton(new Image(CotrixManagerResources.INSTANCE.edit()));
		fullEditButton.setStyleName(ATTRIBUTE_ROW_STYLE.button());
		fullEditButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireButtonClicked(Button.FULL_EDIT);
			}
		});*/
	}

	/**
	 * @param listener the listener to set
	 */
	public void addListener(ConstraintRowListener listener) {
		this.listeners.add(listener);
	}
	

	public void removeListener(ConstraintRowListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setup() {
			
		addCell(LABEL_COL, label);
		setCellStyle(LABEL_COL, propertyGridStyles.header());
		table.getCellFormatter().getElement(rowIndex, LABEL_COL).getStyle().setBackgroundColor("#efefef");

		addCell(VALUE_COL, constraintNameEditableLabel);
		setCellStyle(VALUE_COL, propertyGridStyles.valueBoxLeft());
		
		/*addCell(FULL_EDIT_COL, fullEditButton);
		setCellStyle(FULL_EDIT_COL, propertyGridStyles.valueBoxRight() + " "+ATTRIBUTE_ROW_STYLE.buttonCell());	*/	
		
		addCell(DELETE_COL, deleteButton);
		setCellStyle(DELETE_COL, propertyGridStyles.valueBoxRight() + " "+ATTRIBUTE_ROW_STYLE.buttonCell());
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		constraintNameEditableLabel.setReadOnly(readOnly);
		updateValueCellSpan();
	}
	
	public int getRowIndex() {
		Position position = getCellPosition(label);
		return position.getRow();
	}
	
	private void updateValueCellSpan() {
		int span = readOnly?3:1;
		int rowIndex = getRowIndex();
		table.getFlexCellFormatter().setColSpan(rowIndex, VALUE_COL, span);
		if (readOnly) table.getCellFormatter().setStyleName(rowIndex, VALUE_COL, propertyGridStyles.value());
		else table.getCellFormatter().setStyleName(rowIndex, VALUE_COL, propertyGridStyles.valueBoxLeft());
		
		table.getCellFormatter().setVisible(rowIndex, DELETE_COL, !readOnly);
		table.getCellFormatter().setVisible(rowIndex, FULL_EDIT_COL, !readOnly);
	}
	
	public String getConstraintName() {
		return constraintNameListBox.getItemText(constraintNameListBox.getSelectedIndex());
	}
	
	public void setConstraintName(String constraintName) {
		for (int i = 0; i<constraintNameListBox.getItemCount(); i++) {
			if (constraintNameListBox.getItemText(i).equals(constraintName)) {
				constraintNameListBox.setSelectedIndex(i);
				break;
			}
		}
		constraintNameEditableLabel.setText(constraintName);
	}
	
	private void fireValueChanged() {
		for (ConstraintRowListener listener:listeners) listener.onValueChanged(this);
	}
	
	private void fireButtonClicked(Button button) {
		for (ConstraintRowListener listener:listeners) listener.onButtonClicked(this, button);
	}
	
	public void setValid(boolean valid) {
		label.setStyleName(errorStyle, !valid);
		/*Element rowElement = table.getRowFormatter().getElement(getRowIndex());
		if (valid) rowElement.addClassName(errorStyle);
		else rowElement.removeClassName(errorStyle);*/
	}
	
	public enum Button {DELETE, FULL_EDIT;}
	
	public interface ConstraintRowListener {
		public void onButtonClicked(ConstraintRow row, Button button);
		public void onValueChanged(ConstraintRow row);
	}
}