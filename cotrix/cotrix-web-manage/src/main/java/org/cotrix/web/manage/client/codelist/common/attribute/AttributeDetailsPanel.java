/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import java.util.Collection;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.client.widgets.EditableLabel;
import org.cotrix.web.common.client.widgets.LanguageListBox;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameValue;
import org.cotrix.web.manage.client.codelist.cache.AttributeTypesCache;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeTypeSuggestOracle.AttributeTypeSuggestion;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SuggestListBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDetailsPanel extends Composite implements HasValueChangeHandlers<Void>{

	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameValue CODE_NAME_TYPE = new CodeNameValue();

	private static AttributeDetailsPanelUiBinder uiBinder = GWT.create(AttributeDetailsPanelUiBinder.class);

	interface AttributeDetailsPanelUiBinder extends UiBinder<Widget, AttributeDetailsPanel> {}

	interface Style extends CssResource {
		String error();
		String editor();
	}
	
	@UiField EditableLabel definitionBoxContainer;
	@UiField(provided=true) SuggestListBox definitionBox;
	@UiField Image definitionBoxLoader;
	private UIAttributeType selectedDefinition;

	@UiField EditableLabel nameBoxContainer;
	@UiField(provided=true) SuggestListBox nameBox;
	private boolean nameBoxReadOnly;
	
	@UiField EditableLabel typeBoxContainer;
	@UiField TextBox typeBox;
	
	@UiField EditableLabel languageBoxContainer;
	@UiField LanguageListBox languageBox;
	private boolean languageBoxReadOnly;
	
	@UiField EditableLabel valueBoxContainer;
	@UiField TextBox valueBox;

	@UiField Style style;
	
	private AttributeTypesCache attributeTypesCache;
	private AttributeTypeSuggestOracle attributeTypeSuggestOracle;
	
	private boolean readOnly = false;

	public AttributeDetailsPanel(AttributeNameSuggestOracle oracle, AttributeTypesCache attributeTypesCache) {

		nameBox = new SuggestListBox(oracle);
		
		this.attributeTypesCache = attributeTypesCache;
		this.attributeTypeSuggestOracle = new AttributeTypeSuggestOracle();
		definitionBox = new SuggestListBox(attributeTypeSuggestOracle);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		setupDefinitionField();
		setupNameField();
		setupTypeField();
		setupLanguageField();
		setupValueField();
	}
	
	private void setupDefinitionField() {
		
		definitionBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				AttributeTypeSuggestion suggestion = (AttributeTypeSuggestion) event.getSelectedItem();
				definitionBoxContainer.setText(suggestion.getDisplayString());
				if (suggestion == AttributeTypeSuggestOracle.NONE) setDefinitionNone();
				else setDefinition(suggestion.getAttributeType());
			}
		});
	}
	
	private void setDefinitionLoader(boolean visible) {
		definitionBoxLoader.setVisible(visible);
		definitionBoxContainer.setVisible(!visible);
	}
	
	private void loadDefinitions(final String definitionId) {
		setDefinitionLoader(true);
		attributeTypesCache.getItems(new AsyncCallback<Collection<UIAttributeType>>() {
			
			@Override
			public void onSuccess(Collection<UIAttributeType> result) {
				attributeTypeSuggestOracle.loadCache(result);
				selectDefinition(definitionId);
				setDefinitionLoader(false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
	
	private void setDefinitionNone() {
		definitionBoxContainer.setText(AttributeTypeSuggestOracle.NONE.getDisplayString());
		definitionBox.setValue(AttributeTypeSuggestOracle.NONE.getDisplayString());
		selectedDefinition = null;
		
		setName("");
		nameBoxReadOnly = false;
		
		setLanguage(Language.NONE);
		languageBoxReadOnly = false;

		syncNameLanguageFields();
		
		fireChange();
	}
	
	private void syncNameLanguageFields() {
		
		setNameReadOnly(readOnly || nameBoxReadOnly);
		setLanguageReadOnly(readOnly || languageBoxReadOnly);
		
	}
	
	private void selectDefinition(String definitionId) {
		Log.trace("selectDefinition definitionId: "+definitionId);
		UIAttributeType definition = attributeTypesCache.getItem(definitionId);
		Log.trace("found definition in cache: "+definition);
		if (definition == null) setDefinitionNone();
		else {
			definitionBoxContainer.setText(AttributeTypeSuggestion.toDisplayString(definition));
			definitionBox.setValue(AttributeTypeSuggestion.toDisplayString(definition));
			setDefinition(definition);
		}
	}
	
	private void setDefinition(UIAttributeType definition) {
		Log.trace("setting definition to "+definition);
		
		selectedDefinition = definition;
		
		setName(ValueUtils.getLocalPart(definition.getName()));
		nameBoxReadOnly = true;
		
		setLanguage(definition.getLanguage());
		languageBoxReadOnly = true;
		
		String currentValue = getValue();
		String defaultValue = definition.getDefaultValue();
		
		if ((currentValue == null || currentValue.isEmpty()) 
				&& (defaultValue !=null && !defaultValue.isEmpty())) 
			setValue(defaultValue);
		
		syncNameLanguageFields();
		
		fireChange();
	}
	
	private void setupNameField() {
		nameBox.addValueChangeHandler(nameBoxContainer);
		nameBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				nameBoxContainer.setText(event.getSelectedItem().getDisplayString());
				fireChange();
			}
		});
		nameBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fireChange();
			}
		});
		
		nameBox.getValueBox().addKeyUpHandler(new KeyUpHandler() {
			
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
	
	private void setupValueField() {
		valueBox.addValueChangeHandler(valueBoxContainer);
		valueBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				fireChange();
			}
		});
		
		valueBox.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireChange();
			}
		});
	}
	
	public UIAttributeType getDefinition() {
		return selectedDefinition;
	}
	
	public void setDefinitionId(String definitionId) {
		loadDefinitions(definitionId);
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
	
	public String getValue() {
		return valueBox.getValue();
	}
	
	public void setValue(String value) {
		valueBox.setValue(value, false);
		valueBoxContainer.setText(value);
	}
	
	public void setValueFieldValid(boolean valid) {
		valueBox.setStyleName(style.error(), !valid);
	}
	
	public void setNameReadOnly(boolean readOnly) {
		nameBoxContainer.setReadOnly(readOnly);
		if (readOnly) nameBox.setStyleName(style.error(), false);
	}
	
	public void setLanguageReadOnly(boolean readOnly) {
		languageBoxContainer.setReadOnly(readOnly);
		if (readOnly) languageBox.setStyleName(style.error(), false);
	}

	public void setReadOnly(boolean readOnly) {
		
		this.readOnly = readOnly;
		
		definitionBoxContainer.setReadOnly(readOnly);
		if (readOnly) definitionBox.setStyleName(style.error(), false);
		
		setNameReadOnly(readOnly);
		
		typeBoxContainer.setReadOnly(readOnly);
		if (readOnly) typeBox.setStyleName(style.error(), false);
		
		setLanguageReadOnly(readOnly);

		valueBoxContainer.setReadOnly(readOnly);
		if (readOnly) valueBox.setStyleName(style.error(), false);
		
		if (!readOnly) syncNameLanguageFields();
	}

	private void fireChange() {
		ValueChangeEvent.fire(this, null);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
