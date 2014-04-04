/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.List;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.link.AttributeType;
import org.cotrix.web.manage.client.codelist.link.AttributeSuggestOracle.AttributeSuggestion;
import org.cotrix.web.manage.client.codelist.link.CodelistSuggestOracle.CodelistSuggestion;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypeDetailsPanel extends Composite implements HasValueChangeHandlers<LinkTypeDetails>{
	
	public static final String NONE_FUNCTION = "NONE";
	public static final String OTHER_FUNCTION = "OTHER";

	private static LinkTypeDetailsPanelUiBinder uiBinder = GWT.create(LinkTypeDetailsPanelUiBinder.class);

	interface LinkTypeDetailsPanelUiBinder extends UiBinder<Widget, LinkTypeDetailsPanel> {}
	
	interface Style extends CssResource {
		String error();
		String editor();
	}
	
	public enum ValueType {NAME, ATTRIBUTE, LINK};
	
	@UiField EditableLabel nameContainer;
	@UiField TextBox name;
	
	@UiField EditableLabel codelistContainer;
	@UiField(provided=true) SuggestBox codelist;
	@UiField Image codelistLoader;

	@UiField EditableLabel valueTypeContainer;
	@UiField ListBox valueType;
	

	@UiField TableRowElement attributeRow;
	@UiField EditableLabel attributeContainer;	
	@UiField(provided=true) SuggestBox attribute;
	@UiField Image attributeLoader;
	
	@UiField EditableLabel valueFunctionContainer;
	@UiField ListBox valueFunction;
	
	@UiField TableRowElement functionRow;
	@UiField EditableLabel functionContainer;
	@UiField TextBox function;
	
	
	@UiField Style style;
	
	private UICodelist selectedCodelist;
	private AttributeType selectedAttribute;
	
	private CodelistSuggestOracle codelistSuggestOracle = new CodelistSuggestOracle();	
	private AttributeSuggestOracle attributeSuggestOracle = new AttributeSuggestOracle();
	
	private CodelistInfoProvider codelistInfoProvider;
	
	public LinkTypeDetailsPanel(CodelistInfoProvider codelistInfoProvider) {
		
		this.codelistInfoProvider = codelistInfoProvider;
		
		CotrixManagerResources.INSTANCE.css().ensureInjected();
		CotrixManagerResources.INSTANCE.propertyGrid().ensureInjected();
		CommonResources.INSTANCE.css().ensureInjected();
		
		codelist = new SuggestBox(codelistSuggestOracle);
		codelist.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				selectedCodelist = null;
				fireChange();
			}
		});
		
		codelist.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				CodelistSuggestion suggestion = (CodelistSuggestion) event.getSelectedItem();
				selectedCodelist = suggestion.getCodelist();
				updateAttribute(selectedCodelist.getId());
				codelistContainer.setText(CodelistSuggestion.toDisplayString(selectedCodelist));
				fireChange();
			}
		});

		attribute = new SuggestBox(attributeSuggestOracle);
		attribute.setAutoSelectEnabled(true);
		attribute.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				AttributeSuggestion suggestion = (AttributeSuggestion) event.getSelectedItem();
				selectedAttribute = suggestion.getAttribute();
				fireChange();
			}
		});
		
		attribute.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				selectedAttribute = null;
				attributeContainer.setText(AttributeSuggestion.toDetailsString(selectedAttribute));
				fireChange();
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		
		name.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				nameContainer.setText(event.getValue());
				fireChange();
			}
			
		});
		
		valueType.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateValueTypeSubPanels();
				valueTypeContainer.setText(getValueType().toString());
				fireChange();
			}
		});
		
		valueType.setSelectedIndex(0);
		updateValueTypeSubPanels();
		
		valueFunction.addItem("None", NONE_FUNCTION);
		valueFunction.addItem("UpperCase", "");
		valueFunction.addItem("LowerCase", "");
		valueFunction.addItem("Expression...", OTHER_FUNCTION);
		
		valueFunction.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateValueFunctionSubPanels();
				valueFunctionContainer.setText(getValueFunction());
				fireChange();
			}
		});
		
		valueFunction.setSelectedIndex(0);
		updateValueFunctionSubPanels();
		
		function.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				functionContainer.setText(event.getValue());
			}
		});
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				loadCodelists();
			}
		});
	}
	
	private final AsyncCallback<List<UICodelist>> codelistCallBack = new AsyncCallback<List<UICodelist>>() {
		
		@Override
		public void onSuccess(List<UICodelist> result) {
			codelistSuggestOracle.loadCache(result);
			codelistLoader.setVisible(false);
			codelistContainer.setVisible(true);
		}
		
		@Override
		public void onFailure(Throwable caught) {
			
		}
	};
	
	private void loadCodelists() {
		codelistLoader.setVisible(true);
		codelistContainer.setVisible(false);
		
		codelistInfoProvider.getCodelists(codelistCallBack);
	}
	
	private ValueType getValueType() {
		String value = valueType.getValue(valueType.getSelectedIndex());
		return ValueType.valueOf(value);
	}
	
	private void updateValueTypeSubPanels() {
		ValueType value = getValueType();
		setAttributeBoxVisible(value==ValueType.ATTRIBUTE);
	}
	
	private void updateValueFunctionSubPanels() {
		String function = valueFunction.getValue(valueFunction.getSelectedIndex());
		setFunctionBoxVisible(OTHER_FUNCTION.equals(function));
	}
	
	private void setAttributeBoxVisible(boolean visible) {
		//mainPanel.getRowFormatter().setVisible(3, visible);
		UIObject.setVisible(attributeRow, visible);
	}
	
	private void setFunctionBoxVisible(boolean visible) {
		//mainPanel.getRowFormatter().setVisible(5, visible);
		UIObject.setVisible(functionRow, visible);
	}
	
	private String getValueFunction() {
		String selectedFunction = valueFunction.getValue(valueFunction.getSelectedIndex());
		if (NONE_FUNCTION.equals(selectedFunction)) return null;
		if (OTHER_FUNCTION.equals(selectedFunction)) return OTHER_FUNCTION;
		return selectedFunction;
	}
	
	private AsyncCallback<List<AttributeType>> attributesCallBack = new AsyncCallback<List<AttributeType>>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(List<AttributeType> result) {
			attributeSuggestOracle.loadCache(result);
			
			if (!result.contains(selectedAttribute)) {
				selectedAttribute = null;
				attribute.setValue("");
				attributeContainer.setText("");
			}
			
			attributeLoader.setVisible(false);
			attributeContainer.setVisible(true);
		}
	};
	
	private void updateAttribute(final String codelistId) {
		attributeLoader.setVisible(true);
		attributeContainer.setVisible(false);
		
		codelistInfoProvider.getAttributes(codelistId, attributesCallBack);
	}
	
	public void setValidName(boolean valid) {
		System.out.println("setValidName valid: "+valid);
		name.setStyleName(style.error(), !valid);
	}
	
	public void setValidCodelist(boolean valid) {
		System.out.println("setValidCodelist valid: "+valid);
		codelist.setStyleName(style.error(), !valid);
	}
	
	public void setValidAttribute(boolean valid) {
		System.out.println("setValidAttribute valid: "+valid);
		attribute.setStyleName(style.error(), !valid);
	}
	
	public void setValidFunction(boolean valid) {
		System.out.println("setValidFunction valid: "+valid);
		function.setStyleName(style.error(), !valid);
	}
	
	public void setReadOnly(boolean readOnly) {
		nameContainer.setReadOnly(readOnly);
		if (readOnly) name.setStyleName(style.error(), false);
		
		codelistContainer.setReadOnly(readOnly);
		if (readOnly) codelist.setStyleName(style.error(), false);
		
		valueTypeContainer.setReadOnly(readOnly);
		
		attributeContainer.setReadOnly(readOnly);
		if (readOnly) attribute.setStyleName(style.error(), false);
		
		valueFunctionContainer.setReadOnly(readOnly);
		
		functionContainer.setReadOnly(readOnly);
		if (readOnly) function.setStyleName(style.error(), false);
	}
	
	public void clear() {
		name.setValue("", false);
		codelist.getValueBox().setValue("", false);
		selectedCodelist = null;
		valueType.setItemSelected(0, true);
		attribute.getValueBox().setValue("", false);
		selectedAttribute = null;
		valueFunction.setItemSelected(0, true);
		function.setValue("", false);
	}
	
	public void setDetails(LinkTypeDetails details) {
		name.setValue(details.getName(), false);
		nameContainer.setText(details.getName());
		
		selectedCodelist = details.getCodelist();
		codelist.getValueBox().setValue(CodelistSuggestion.toDisplayString(selectedCodelist), false);
		codelistContainer.setText(CodelistSuggestion.toDisplayString(selectedCodelist));
		
		setValueType(details.getValueType());
		valueTypeContainer.setText(details.getValueType().toString());
		
		selectedAttribute = details.getAttribute();
		attribute.getValueBox().setValue(AttributeSuggestion.toDetailsString(selectedAttribute), false);
		updateAttribute(selectedCodelist.getId());
		attributeContainer.setText(AttributeSuggestion.toDetailsString(selectedAttribute));
		
		setValueFunction(details.getFunction());
		valueFunctionContainer.setText(details.getFunction());
		
		function.setValue(details.getCustomFunction(), false);
		functionContainer.setText(details.getCustomFunction());
	}
	
	private void setValueType(ValueType type) {
		selecteItem(valueType, type.toString());
		updateValueTypeSubPanels();
	}
	
	private void setValueFunction(String functionName) {
		//TODO better handle other function
		selecteItem(valueFunction, functionName==null?NONE_FUNCTION:functionName);
		updateValueFunctionSubPanels();
	}
	
	private void selecteItem(ListBox listBox, String itemValue) {
		for (int i = 0; i<listBox.getItemCount(); i++) {
			if (listBox.getValue(i).equals(itemValue)) {
				listBox.setSelectedIndex(i);
				return;
			}
		}
		throw new IllegalArgumentException("Unknown itemValue "+itemValue);
	}

	public LinkTypeDetails getDetails() {
		
		String customFunction = function.isVisible()?null:function.getValue();
		return new LinkTypeDetails(name.getValue(), selectedCodelist, getValueType(), selectedAttribute, getValueFunction(), customFunction);
	}
	
	private void fireChange() {
		ValueChangeEvent.fire(this, getDetails());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<LinkTypeDetails> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

}
