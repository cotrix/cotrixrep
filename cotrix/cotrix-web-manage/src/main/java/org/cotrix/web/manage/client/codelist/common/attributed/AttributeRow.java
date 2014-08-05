/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attributed;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.FadeAnimation;
import org.cotrix.web.common.client.util.FadeAnimation.Speed;
import org.cotrix.web.common.client.widgets.AdvancedTextBox;
import org.cotrix.web.common.client.widgets.UIQNameBox;
import org.cotrix.web.common.client.widgets.table.AbstractRow;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.resources.CotrixManagerResources.AttributeRowStyle;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeRow extends AbstractRow {
	
	private static final AttributeRowStyle ATTRIBUTE_ROW_STYLE = CotrixManagerResources.INSTANCE.attributeRow();

	private static final int NAME_COL = 0;
	private static final int VALUE_COL = 1;
	private static final int DELETE_COL = 2;
	private static final int FULL_EDIT_COL = 3;
	
	private AttributeRowListener listener;
	private ValueChangeHandler<String> changeHandler;
	
	private UIQNameBox nameTextBox;
	
	private AdvancedTextBox valueTextBox;
	
	private FadeAnimation deleteButtonAnimation;
	private PushButton deleteButton;
	
	private FadeAnimation fullEditButtonAnimation;
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
		
		KeyUpHandler keyUpHandler = new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireValueChanged();
			}
		};
		
		nameTextBox = new UIQNameBox();
		nameTextBox.setHeight("31px");
		nameTextBox.setPlaceholder("name");
		nameTextBox.setTitle("The name of this attribute");
		nameTextBox.setStyleName(CotrixManagerResources.INSTANCE.detailsPanelStyle().textbox());
		nameTextBox.addValueChangeHandler(new ValueChangeHandler<UIQName>() {

			@Override
			public void onValueChange(ValueChangeEvent<UIQName> event) {
				fireValueChanged();
			}
		});
		nameTextBox.addKeyUpHandler(keyUpHandler);
		
		valueTextBox = new AdvancedTextBox();
		valueTextBox.setHeight("31px");
		valueTextBox.setPlaceholder("value");
		valueTextBox.setTitle("The value of this attribute");
		valueTextBox.setStyleName(CotrixManagerResources.INSTANCE.detailsPanelStyle().textbox());
		valueTextBox.addValueChangeHandler(changeHandler);
		valueTextBox.addKeyUpHandler(keyUpHandler);
		
		deleteButton = new PushButton(new Image(CommonResources.INSTANCE.minus()));
		deleteButton.setStyleName(ATTRIBUTE_ROW_STYLE.button());
		deleteButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireButtonClicked(Button.DELETE);
			}
		});
		deleteButtonAnimation = new FadeAnimation(deleteButton.getElement());
		
		fullEditButton = new PushButton(new Image(CotrixManagerResources.INSTANCE.edit()));
		fullEditButton.setStyleName(ATTRIBUTE_ROW_STYLE.button());
		fullEditButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				fireButtonClicked(Button.FULL_EDIT);
			}
		});
		fullEditButtonAnimation = new FadeAnimation(fullEditButton.getElement());
	}

	/**
	 * @param listener the listener to set
	 */
	public void setListener(AttributeRowListener listener) {
		this.listener = listener;
	}

	@Override
	public void setup() {
			
		addCell(NAME_COL, nameTextBox);
		setCellStyle(NAME_COL, CotrixManagerResources.INSTANCE.detailsPanelStyle().valueCell());

		addCell(VALUE_COL, valueTextBox);
		setCellStyle(VALUE_COL, CotrixManagerResources.INSTANCE.detailsPanelStyle().valueCellLeft());
		
		addCell(DELETE_COL, deleteButton);
		setCellStyle(DELETE_COL, CotrixManagerResources.INSTANCE.detailsPanelStyle().valueCellCenter() + " "+ATTRIBUTE_ROW_STYLE.buttonCell());
		
		addCell(FULL_EDIT_COL, fullEditButton);
		setCellStyle(FULL_EDIT_COL, CotrixManagerResources.INSTANCE.detailsPanelStyle().valueCellRight() + " "+ATTRIBUTE_ROW_STYLE.buttonCell());		
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		nameTextBox.setEnabled(!readOnly);
		valueTextBox.setEnabled(!readOnly);
		updateValueCellSpan();
	}
	
	public int getRowIndex() {
		Position position = getCellPosition(nameTextBox);
		return position.getRow();
	}

	private void updateValueCellSpan() {
		int span = readOnly?3:1;
		int rowIndex = getRowIndex();
		table.getFlexCellFormatter().setColSpan(rowIndex, VALUE_COL, span);
		if (readOnly) table.getCellFormatter().setStyleName(rowIndex, VALUE_COL, CotrixManagerResources.INSTANCE.detailsPanelStyle().valueCell());
		else table.getCellFormatter().setStyleName(rowIndex, VALUE_COL, CotrixManagerResources.INSTANCE.detailsPanelStyle().valueCellLeft());
		
		table.getCellFormatter().setVisible(rowIndex, DELETE_COL, !readOnly);
		table.getCellFormatter().setVisible(rowIndex, FULL_EDIT_COL, !readOnly);
		
		if (readOnly) {
			deleteButtonAnimation.setVisibility(false, Speed.IMMEDIATE);
			fullEditButtonAnimation.setVisibility(false, Speed.IMMEDIATE);
		} else {
			deleteButtonAnimation.setVisibility(true, Speed.VERY_FAST);
			fullEditButtonAnimation.setVisibility(true, Speed.VERY_FAST);
		}
	}
	
	public UIQName getName() {
		return nameTextBox.getValue();
	}
	
	public void setName(UIQName name) {
		nameTextBox.setValue(name);
	}
	
	public String getValue() {
		return valueTextBox.getValue();
	}
	
	public void setValue(String value) {
		valueTextBox.setValue(value);
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