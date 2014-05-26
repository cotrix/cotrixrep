/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attributetype;

import org.cotrix.web.common.client.widgets.EditableLabel;
import org.cotrix.web.common.client.widgets.LanguageListBox;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypeDetailsPanel extends Composite implements HasValueChangeHandlers<Void>{

	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameValue CODE_NAME_TYPE = new CodeNameValue();

	private static AtttributeDetailsPanelUiBinder uiBinder = GWT.create(AtttributeDetailsPanelUiBinder.class);

	interface AtttributeDetailsPanelUiBinder extends UiBinder<Widget, AttributeTypeDetailsPanel> {}

	interface Style extends CssResource {
		String error();
		String editor();
	}

	@UiField EditableLabel nameBoxContainer;
	@UiField TextBox nameBox;
	
	@UiField EditableLabel typeBoxContainer;
	@UiField TextBox typeBox;
	
	@UiField EditableLabel languageBoxContainer;
	@UiField LanguageListBox languageBox;

	@UiField Style style;

	public AttributeTypeDetailsPanel() {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		setupNameField();
		setupTypeField();
		setupLanguageField();
	}
	
	private void setupNameField() {
		nameBox.addValueChangeHandler(nameBoxContainer);
		nameBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fireChange();
			}
		});
		
		nameBox.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireChange();
			}
		});
	}
	
	private void setupTypeField() {
		typeBox.addValueChangeHandler(typeBoxContainer);
		typeBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fireChange();
			}
		});
		
		typeBox.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireChange();
			}
		});
	}
	
	private void setupLanguageField() {
		languageBox.addValueChangeHandler(new ValueChangeHandler<Language>() {

			@Override
			public void onValueChange(ValueChangeEvent<Language> event) {
				languageBoxContainer.setText(event.getValue().getName());
				fireChange();
			}
		});
		
	}
	
	public String getName() {
		return nameBox.getValue();
	}
	
	public void setName(String name) {
		nameBox.setValue(name);
		nameBoxContainer.setText(name);
	}
	
	public void setNameFieldValid(boolean valid) {
		nameBox.setStyleName(style.error(), !valid);
	}
	
	public String getType() {
		return typeBox.getValue();
	}
	
	public void setType(String type) {
		typeBox.setValue(type, false);
		typeBoxContainer.setText(type);
	}
	
	public void setTypeFieldValid(boolean valid) {
		typeBox.setStyleName(style.error(), !valid);
	}
	
	public Language getLanguage() {
		return languageBox.getValue();
	}
	
	public void setLanguage(Language language) {
		languageBox.setValue(language);
		languageBoxContainer.setText(language.getName());
	}
	
	public void setLanguageFieldValid(boolean valid) {
		languageBox.setStyleName(style.error(), !valid);
	}

	public void setReadOnly(boolean readOnly) {
		
		nameBoxContainer.setReadOnly(readOnly);
		if (readOnly) nameBox.setStyleName(style.error(), false);
		
		typeBoxContainer.setReadOnly(readOnly);
		if (readOnly) typeBox.setStyleName(style.error(), false);
		
		languageBoxContainer.setReadOnly(readOnly);
		if (readOnly) languageBox.setStyleName(style.error(), false);
	}

	private void fireChange() {
		ValueChangeEvent.fire(this, null);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

}
