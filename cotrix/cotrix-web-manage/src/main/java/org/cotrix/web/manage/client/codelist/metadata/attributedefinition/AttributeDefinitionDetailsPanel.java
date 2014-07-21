/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributedefinition;

import java.util.List;

import org.cotrix.web.common.client.widgets.AdvancedIntegerBox;
import org.cotrix.web.common.client.widgets.AdvancedTextBox;
import org.cotrix.web.common.client.widgets.EnumListBox;
import org.cotrix.web.common.client.widgets.LanguageListBox;
import org.cotrix.web.common.client.widgets.table.CellContainer;
import org.cotrix.web.common.client.widgets.table.Table;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIConstraint;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;
import org.cotrix.web.common.shared.codelist.linkdefinition.CodeNameValue;
import org.cotrix.web.manage.client.codelist.common.DetailsPanelStyle;
import org.cotrix.web.manage.client.codelist.common.SuggestListBox;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;
import org.cotrix.web.manage.client.codelist.metadata.attributedefinition.constraint.ConstraintsPanel;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionDetailsPanel extends Composite implements HasValueChangeHandlers<Void>{

	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameValue CODE_NAME_TYPE = new CodeNameValue();

	private static AttributeDetailsPanelUiBinder uiBinder = GWT.create(AttributeDetailsPanelUiBinder.class);

	interface AttributeDetailsPanelUiBinder extends UiBinder<Widget, AttributeDefinitionDetailsPanel> {}

	@UiField Table table;

	@UiField AdvancedTextBox nameBox;
	
	@UiField(provided=true) SuggestListBox typeBox;
	
	@UiField LanguageListBox languageBox;
	
	@UiField AdvancedTextBox defaultBox;

	@UiField(provided = true) EnumListBox<Occurrences> occurrencesBox;
	
	@UiField CellContainer occurrencesMinRow;
	@UiField AdvancedIntegerBox occurrencesMin;
	
	@UiField CellContainer occurrencesMaxRow;
	@UiField AdvancedIntegerBox occurrencesMax;
	
	private DetailsPanelStyle style = CotrixManagerResources.INSTANCE.detailsPanelStyle();
	
	private ValueChangeHandler<String> changeHandler = new ValueChangeHandler<String>() {

		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			fireChange();
		}
	};
	
	private ConstraintsPanel constraintsPanel;

	public AttributeDefinitionDetailsPanel(AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle) {
		
		occurrencesBox = new EnumListBox<Occurrences>(Occurrences.class, Occurrences.LABEL_PROVIDER);
		
		attributeDescriptionSuggestOracle.setOnlyDefaults(false);
		typeBox = new SuggestListBox(attributeDescriptionSuggestOracle);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		setupNameField();
		setupTypeField();
		setupLanguageField();
		setupDefaultField();
		setupOccurrencesField();
		setupConstraintsPanel();
	}
	
	private void setupNameField() {
		nameBox.addValueChangeHandler(changeHandler);
		
		nameBox.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireChange();
			}
		});
	}
	
	private void setupTypeField() {
		typeBox.addValueChangeHandler(changeHandler);
		
		typeBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				fireChange();
			}
		});		
		
		typeBox.getValueBox().addKeyUpHandler(new KeyUpHandler() {
			
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
				fireChange();
			}
		});
	}
	
	private void setupDefaultField() {
		defaultBox.addValueChangeHandler(changeHandler);
		
		defaultBox.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireChange();
			}
		});
	}
	
	private void setupOccurrencesField() {
		occurrencesBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateMinMaxVisibility(true);
				fireChange();
			}
		});
		
		occurrencesMin.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				fireChange();
			}
		});
		
		occurrencesMax.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				fireChange();
			}
		});
	}
	
	private void updateMinMaxVisibility(boolean clean) {
		Occurrences occurrences = occurrencesBox.getSelectedValue();
		occurrencesMinRow.setVisible(occurrences.isCustomMin());
		if (clean) occurrencesMin.setValue(null);
		
		occurrencesMaxRow.setVisible(occurrences.isCustomMax());
		if (clean) occurrencesMax.setValue(null);
	}
	
	private void setupConstraintsPanel() {
		constraintsPanel = new ConstraintsPanel(table, style.textboxError());
		constraintsPanel.addValueChangeHandler(new ValueChangeHandler<Void>() {

			@Override
			public void onValueChange(ValueChangeEvent<Void> event) {
				fireChange();
			}
		});
	}
	
	
	public String getName() {
		return nameBox.getValue();
	}
	
	public void setName(String name) {
		nameBox.setValue(name);
		nameBox.setTitle(name);
	}
	
	public void focusName() {
		nameBox.setFocus(true);
	}
	
	public void setNameFieldValid(boolean valid) {
		nameBox.setStyleName(style.textboxError(), !valid);
	}
	
	public String getType() {
		return typeBox.getValue();
	}
	
	public void setType(String type) {
		typeBox.setValue(type);
	}
	
	public void setTypeFieldValid(boolean valid) {
		typeBox.setStyleName(style.textboxError(), !valid);
	}
	
	public Language getLanguage() {
		return languageBox.getValue();
	}
	
	public void setLanguage(Language language) {
		languageBox.setValue(language);
	}
	
	public void setLanguageFieldValid(boolean valid) {
		languageBox.setStyleName(style.textboxError(), !valid);
	}
	
	public String getDefault() {
		return defaultBox.getValue();
	}
	
	public void setDefault(String defaultValue) {
		defaultBox.setValue(defaultValue);
		defaultBox.setTitle(defaultValue);
	}
	
	public void setDefaultFieldValid(boolean valid) {
		defaultBox.setStyleName(style.textboxError(), !valid);
	}
	
	public UIRange getRange() {
		Occurrences occurrences = occurrencesBox.getSelectedValue();
		Integer userMin = occurrencesMin.getValue();
		Integer userMax = occurrencesMax.getValue();
		return occurrences.toRange(userMin!=null?userMin:Integer.MIN_VALUE, userMax!=null?userMax:Integer.MIN_VALUE);
	}
	
	public void setRange(UIRange range) {
		Occurrences occurrences = Occurrences.toOccurrences(range);
		occurrencesBox.setSelectedValue(occurrences);
		updateMinMaxVisibility(false);
		
		occurrencesMin.setValue(range.getMin());
		
		occurrencesMax.setValue(range.getMax());
	}
	
	public void setRangeFieldValid(boolean valid) {
		occurrencesMin.setStyleName(style.textboxError(), !valid);
		occurrencesMax.setStyleName(style.textboxError(), !valid);
	}
	
	public List<UIConstraint> getConstraints() {
		return constraintsPanel.getConstraints();
	}
	
	public void setConstraints(List<UIConstraint> constraints) {
		constraintsPanel.setConstraints(constraints);
	}

	public void setReadOnly(boolean readOnly) {
		
		nameBox.setEnabled(!readOnly);
		if (readOnly) nameBox.setStyleName(style.textboxError(), false);
		
		typeBox.setEnabled(!readOnly);
		if (readOnly) typeBox.setStyleName(style.textboxError(), false);
		
		languageBox.setEnabled(!readOnly);
		if (readOnly) languageBox.setStyleName(style.textboxError(), false);
		
		defaultBox.setEnabled(!readOnly);
		if (readOnly) defaultBox.setStyleName(style.textboxError(), false);
		
		occurrencesBox.setEnabled(!readOnly);
		occurrencesMin.setEnabled(!readOnly);
		occurrencesMax.setEnabled(!readOnly);
		if (readOnly) {
			occurrencesMin.setStyleName(style.textboxError(), false);
			occurrencesMax.setStyleName(style.textboxError(), false);
		}
		
		constraintsPanel.setReadOnly(readOnly);
	}

	private void fireChange() {
		ValueChangeEvent.fire(this, null);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
