/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.List;

import org.cotrix.web.common.client.util.LabelProvider;
import org.cotrix.web.common.client.util.ListBoxUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.link.CodeNameType;
import org.cotrix.web.common.shared.codelist.link.UILinkType.UIValueType;
import org.cotrix.web.common.shared.codelist.link.UIValueFunction;
import org.cotrix.web.common.shared.codelist.link.UIValueFunction.Function;
import org.cotrix.web.manage.client.codelist.link.CodelistSuggestOracle.CodelistSuggestion;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
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
public class LinkTypeDetailsPanel extends Composite implements HasValueChangeHandlers<Void>{
	
	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameType CODE_NAME_TYPE = new CodeNameType();

	private static LinkTypeDetailsPanelUiBinder uiBinder = GWT.create(LinkTypeDetailsPanelUiBinder.class);

	interface LinkTypeDetailsPanelUiBinder extends UiBinder<Widget, LinkTypeDetailsPanel> {}
	
	interface Style extends CssResource {
		String error();
		String editor();
	}
	
	@UiField EditableLabel nameBoxContainer;
	@UiField TextBox nameBox;
	
	@UiField EditableLabel codelistBoxContainer;
	@UiField(provided=true) SuggestBox codelistBox;
	@UiField Image codelistBoxLoader;

	@UiField ValueTypePanel valueTypePanel;
	
	@UiField EditableLabel valueFunctionContainer;
	@UiField ListBox valueFunction;
	
	@UiField TableRowElement functionArgumentsRow;
	@UiField FunctionsArgumentsPanels functionArguments;
	
	@UiField Style style;
	
	private UICodelist selectedCodelist;
	
	private CodelistSuggestOracle codelistSuggestOracle = new CodelistSuggestOracle();	
	
	private CodelistInfoProvider codelistInfoProvider;
	
	private LabelProvider<Function> functionLabelProvider = new DefaultFunctionLabelProvider();
	
	public LinkTypeDetailsPanel(CodelistInfoProvider codelistInfoProvider) {
		
		this.codelistInfoProvider = codelistInfoProvider;
	
		createCodelistBox();

		initWidget(uiBinder.createAndBindUi(this));
		
		setupNameBox();
		
		setupValueTypePanel();

		setupFunction();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				loadCodelists();
			}
		});
	}
	
	private void setupNameBox() {
		nameBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				nameBoxContainer.setText(event.getValue());
				fireChange();
			}
		});
	}
	
	public String getName() {
		return nameBox.getValue();
	}
	
	public void setName(String name) {
		this.nameBox.setValue(name, false);
		this.nameBoxContainer.setText(name);
	}

	public void setValidName(boolean valid) {
		nameBox.setStyleName(style.error(), !valid);
	}
	
	
	private void createCodelistBox() {
		codelistBox = new SuggestBox(codelistSuggestOracle);
		codelistBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				selectedCodelist = null;
				fireChange();
			}
		});
		
		codelistBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				CodelistSuggestion suggestion = (CodelistSuggestion) event.getSelectedItem();
				selectedCodelist = suggestion.getCodelist();
				updateValueType(selectedCodelist.getId(), null);
				codelistBoxContainer.setText(CodelistSuggestion.toDisplayString(selectedCodelist));
				fireChange();
			}
		});
	}
	
	public void setCodelist(UICodelist codelist, UIValueType valueType) {
		selectedCodelist = codelist;
		codelistBox.getValueBox().setValue(CodelistSuggestion.toDisplayString(selectedCodelist), false);
		codelistBoxContainer.setText(CodelistSuggestion.toDisplayString(selectedCodelist));
		
		updateValueType(codelist.getId(), valueType);
	}
	
	public UICodelist getCodelist() {
		return selectedCodelist;
	}
	
	public void setValidCodelist(boolean valid) {
		codelistBox.setStyleName(style.error(), !valid);
	}

	private final AsyncCallback<List<UICodelist>> codelistCallBack = new AsyncCallback<List<UICodelist>>() {
		
		@Override
		public void onSuccess(List<UICodelist> result) {
			codelistSuggestOracle.loadCache(result);
			codelistBoxLoader.setVisible(false);
			codelistBoxContainer.setVisible(true);
		}
		
		@Override
		public void onFailure(Throwable caught) {
			
		}
	};
	
	private void loadCodelists() {
		codelistBoxLoader.setVisible(true);
		codelistBoxContainer.setVisible(false);
		codelistInfoProvider.getCodelists(codelistCallBack);
	}
	
	
	private void setupValueTypePanel() {
		valueTypePanel.setCodelistInfoProvider(codelistInfoProvider);
	}
	
	private void updateValueType(final String codelistId, final UIValueType type) {
		valueTypePanel.setCodelist(codelistId, type);
	}
	
	public UIValueType getValueType() {
		return valueTypePanel.getValueType();
	}
	
	public void setValueType(UIValueType type) {
		valueTypePanel.setValueType(type);
	}
	
	
	private void setupFunction() {
		for (Function function:Function.values()) {
			String label = functionLabelProvider.getLabel(function);
			valueFunction.addItem(label, function.toString());
		}
		
		valueFunction.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateValueFunctionSubPanels();
				syncValueFunction();
				fireChange();
			}
		});
		
		valueFunction.setSelectedIndex(0);
		updateValueFunctionSubPanels();
	}
	
	public void setValidFunction(boolean valid) {
		functionArguments.setStyleName(style.error(), !valid);
	}
	
	public UIValueFunction getValueFunction() {
		Function function = getSelectedFunction();
		List<String> arguments = functionArguments.getArgumentsValues();
		return new UIValueFunction(function, arguments);
	}
	
	public void setValueFunction(UIValueFunction valueFunction) {
		Log.trace("setValueFunction valueFunction: "+valueFunction);
		setSelectedFunction(valueFunction.getFunction());
		updateValueFunctionSubPanels();
		functionArguments.setArgumentsValues(valueFunction.getArguments());
		syncValueFunction();
	}	
	
	private void updateValueFunctionSubPanels() {
		Function function = getSelectedFunction();
		setFunctionRowVisible(functionArguments.hasArguments(function));
	}
	
	private void setFunctionRowVisible(boolean visible) {
		UIObject.setVisible(functionArgumentsRow, visible);
	}
	
	private Function getSelectedFunction() {
		String selectedFunction = valueFunction.getValue(valueFunction.getSelectedIndex());
		return Function.valueOf(selectedFunction);
	}
	
	private void setSelectedFunction(Function function) {
		ListBoxUtils.selecteItem(valueFunction, function.toString());
	}
	
	private void syncValueFunction() {
		valueFunctionContainer.setText(functionLabelProvider.getLabel(getSelectedFunction()));
	}
	

	public void setReadOnly(boolean readOnly) {
		nameBoxContainer.setReadOnly(readOnly);
		if (readOnly) nameBox.setStyleName(style.error(), false);
		
		codelistBoxContainer.setReadOnly(readOnly);
		if (readOnly) codelistBox.setStyleName(style.error(), false);
		
		valueTypePanel.setReadOnly(readOnly);
		
		valueFunctionContainer.setReadOnly(readOnly);
		
		functionArguments.setReadOnly(readOnly);
		if (readOnly) functionArguments.setStyleName(style.error(), false);
	}
		
	private void fireChange() {
		ValueChangeEvent.fire(this, null);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}
	
	private class DefaultFunctionLabelProvider implements LabelProvider<Function> {

		@Override
		public String getLabel(Function item) {
			switch (item) {
				case CUSTOM: return "expression...";
				case IDENTITY: return "none";
				case LOWERCASE: return "lowercase";
				case PREFIX: return "prefix";
				case SUFFIX: return "suffix";
				case UPPERCASE: return "uppercase";
			}
			throw new IllegalArgumentException("Unknown function "+item);
		}
		
	}

}
