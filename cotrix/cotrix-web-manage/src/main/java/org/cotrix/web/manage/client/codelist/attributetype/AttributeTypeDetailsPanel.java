/**
 * 
 */
package org.cotrix.web.manage.client.codelist.attributetype;

import org.cotrix.web.common.client.widgets.AdvancedIntegerBox;
import org.cotrix.web.common.client.widgets.EditableLabel;
import org.cotrix.web.common.client.widgets.EnumListBox;
import org.cotrix.web.common.client.widgets.LanguageListBox;
import org.cotrix.web.common.client.widgets.table.CellContainer;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.attributetype.UIRange;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameValue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
	
	@UiField EditableLabel occurrencesBoxContainer;
	@UiField(provided = true) EnumListBox<Occurrences> occurrencesBox;
	
	@UiField CellContainer occurrencesMinRow;
	@UiField EditableLabel occurrencesMinBoxContainer;
	@UiField AdvancedIntegerBox occurrencesMin;
	
	@UiField CellContainer occurrencesMaxRow;
	@UiField EditableLabel occurrencesMaxBoxContainer;
	@UiField AdvancedIntegerBox occurrencesMax;

	@UiField Style style;

	public AttributeTypeDetailsPanel() {
		
		occurrencesBox = new EnumListBox<Occurrences>(Occurrences.class, Occurrences.LABEL_PROVIDER);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		setupNameField();
		setupTypeField();
		setupLanguageField();
		setupOccurrencesField();
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
	
	private void setupOccurrencesField() {
		occurrencesBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				occurrencesBoxContainer.setText(occurrencesBox.getSelectedValue().getLabel());
				updateMinMaxVisibility(true);
				fireChange();
			}
		});
		
		occurrencesMin.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				occurrencesMinBoxContainer.setText(String.valueOf(occurrencesMin.getValue()));
				fireChange();
			}
		});
		
		occurrencesMax.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				occurrencesMaxBoxContainer.setText(String.valueOf(occurrencesMax.getValue()));
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
	
	public UIRange getRange() {
		Occurrences occurrences = occurrencesBox.getSelectedValue();
		Integer userMin = occurrencesMin.getValue();
		Integer userMax = occurrencesMax.getValue();
		return occurrences.toRange(userMin!=null?userMin:Integer.MIN_VALUE, userMax!=null?userMax:Integer.MIN_VALUE);
	}
	
	public void setRange(UIRange range) {
		Occurrences occurrences = Occurrences.toOccurrences(range);
		occurrencesBox.setSelectedValue(occurrences);
		occurrencesBoxContainer.setText(occurrences.getLabel());
		updateMinMaxVisibility(false);
		
		occurrencesMin.setValue(range.getMin());
		occurrencesMinBoxContainer.setText(String.valueOf(range.getMin()));
		
		occurrencesMax.setValue(range.getMax());
		occurrencesMaxBoxContainer.setText(String.valueOf(range.getMax()));
	}
	
	public void setRangeFieldValid(boolean valid) {
		occurrencesMinBoxContainer.setStyleName(style.error(), !valid);
		occurrencesMaxBoxContainer.setStyleName(style.error(), !valid);
	}

	public void setReadOnly(boolean readOnly) {
		
		nameBoxContainer.setReadOnly(readOnly);
		if (readOnly) nameBox.setStyleName(style.error(), false);
		
		typeBoxContainer.setReadOnly(readOnly);
		if (readOnly) typeBox.setStyleName(style.error(), false);
		
		languageBoxContainer.setReadOnly(readOnly);
		if (readOnly) languageBox.setStyleName(style.error(), false);
		
		occurrencesBoxContainer.setReadOnly(readOnly);
		occurrencesMinBoxContainer.setReadOnly(readOnly);
		occurrencesMaxBoxContainer.setReadOnly(readOnly);
		if (readOnly) {
			occurrencesMin.setStyleName(style.error(), false);
			occurrencesMax.setStyleName(style.error(), false);
		}
	}

	private void fireChange() {
		ValueChangeEvent.fire(this, null);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
}
