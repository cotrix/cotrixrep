/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.linkdefinition;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.util.LabelProvider;
import org.cotrix.web.common.client.util.ListBoxUtils;
import org.cotrix.web.common.client.widgets.AdvancedTextBox;
import org.cotrix.web.common.client.widgets.table.CellContainer;
import org.cotrix.web.common.client.widgets.table.Table;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.linkdefinition.CodeNameValue;
import org.cotrix.web.common.shared.codelist.linkdefinition.UIValueFunction;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition.UIValueType;
import org.cotrix.web.common.shared.codelist.linkdefinition.UIValueFunction.Function;
import org.cotrix.web.manage.client.codelist.common.AttributesPanel;
import org.cotrix.web.manage.client.codelist.common.DetailsPanelStyle;
import org.cotrix.web.manage.client.codelist.common.SuggestListBox;
import org.cotrix.web.manage.client.codelist.metadata.linkdefinition.CodelistSuggestOracle.CodelistSuggestion;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.Attributes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkDefinitionDetailsPanel extends Composite implements HasValueChangeHandlers<Void>{

	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameValue CODE_NAME_TYPE = new CodeNameValue();

	private static LinkDefinitionDetailsPanelUiBinder uiBinder = GWT.create(LinkDefinitionDetailsPanelUiBinder.class);

	interface LinkDefinitionDetailsPanelUiBinder extends UiBinder<Widget, LinkDefinitionDetailsPanel> {}

	@UiField Table table;

	@UiField AdvancedTextBox nameBox;

	@UiField(provided=true) SuggestListBox codelistBox;
	@UiField Image codelistBoxLoader;

	@UiField ValueTypePanel valueTypePanel;

	@UiField ListBox valueFunction;

	@UiField CellContainer functionArgumentsRow;
	@UiField Label functionArgumentLabel;
	@UiField TextBox functionArgumentBox;

	private AttributesPanel attributesPanel;

	private DetailsPanelStyle style = CotrixManagerResources.INSTANCE.detailsPanelStyle();

	private UICodelist selectedCodelist;

	private CodelistSuggestOracle codelistSuggestOracle = new CodelistSuggestOracle();	

	private LinkDefinitionsCodelistInfoProvider codelistInfoProvider;

	private LabelProvider<Function> functionLabelProvider = new DefaultFunctionLabelProvider();
	
	private ValueChangeHandler<String> changeHandler = new ValueChangeHandler<String>() {

		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			fireChange();
		}
	};

	public LinkDefinitionDetailsPanel(LinkDefinitionsCodelistInfoProvider codelistInfoProvider) {

		this.codelistInfoProvider = codelistInfoProvider;

		createCodelistBox();

		initWidget(uiBinder.createAndBindUi(this));

		setupNameBox();

		setupValueTypePanel();

		setupFunction();

		setupAttributesPanel();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				loadCodelists();
			}
		});
	}

	private void setupNameBox() {
		nameBox.addValueChangeHandler(changeHandler);
		
		nameBox.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				fireChange();
			}
		});
	}

	public String getName() {
		return nameBox.getValue();
	}

	public void setName(String name) {
		this.nameBox.setValue(name, false);
	}
	
	public void focusName() {
		nameBox.setFocus(true);
	}

	public void setValidName(boolean valid) {
		nameBox.setStyleName(style.textboxError(), !valid);
	}


	private void createCodelistBox() {
		codelistBox = new SuggestListBox(codelistSuggestOracle);
		codelistBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				selectedCodelist = null;
				updateValueType(null, null);
				fireChange();
			}
		});

		codelistBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				CodelistSuggestion suggestion = (CodelistSuggestion) event.getSelectedItem();
				selectedCodelist = suggestion.getCodelist();
				updateValueType(selectedCodelist.getId(), null);
				fireChange();
			}
		});
	}

	public void setCodelist(UICodelist codelist, UIValueType valueType) {
		selectedCodelist = codelist;
		codelistBox.getValueBox().setValue(CodelistSuggestion.toDisplayString(selectedCodelist), false);

		if (codelist!=null) updateValueType(codelist.getId(), valueType);
	}

	public UICodelist getCodelist() {
		return selectedCodelist;
	}

	public void setValidCodelist(boolean valid) {
		codelistBox.setStyleName(style.textboxError(), !valid);
	}

	public void setCodelistReadonly(boolean readOnly) {
		codelistBox.setEnabled(!readOnly);
		if (readOnly) codelistBox.setStyleName(style.textboxError(), false);
	}

	private final AsyncCallback<List<UICodelist>> codelistCallBack = new AsyncCallback<List<UICodelist>>() {

		@Override
		public void onSuccess(List<UICodelist> result) {
			codelistSuggestOracle.loadCache(result);
			codelistBoxLoader.setVisible(false);
			codelistBox.setVisible(true);
		}

		@Override
		public void onFailure(Throwable caught) {

		}
	};

	private void loadCodelists() {
		codelistBoxLoader.setVisible(true);
		codelistBox.setVisible(false);
		codelistInfoProvider.getCodelists(codelistCallBack);
	}


	private void setupValueTypePanel() {
		valueTypePanel.setCodelistInfoProvider(codelistInfoProvider);
		valueTypePanel.addValueChangeHandler(new ValueChangeHandler<UIValueType>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<UIValueType> event) {
				fireChange();
			}
		});
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
				fireChange();
			}
		});

		valueFunction.setSelectedIndex(0);
		updateValueFunctionSubPanels();
		
		functionArgumentBox.addValueChangeHandler(changeHandler);
	}

	public void setValidFunction(boolean valid) {
		functionArgumentBox.setStyleName(style.textboxError(), !valid);
	}

	public UIValueFunction getValueFunction() {
		Function function = getSelectedFunction();
		List<String> arguments = new ArrayList<String>();
		if (functionArgumentsRow.isVisible()) arguments.add(functionArgumentBox.getValue());
		return new UIValueFunction(function, arguments);
	}

	public void setValueFunction(UIValueFunction valueFunction) {
		Log.trace("setValueFunction valueFunction: "+valueFunction);
		if (valueFunction!=null) {
			setSelectedFunction(valueFunction.getFunction());
			updateValueFunctionSubPanels();
			if (!valueFunction.getArguments().isEmpty()) {
				String argument = valueFunction.getArguments().get(0);
				functionArgumentBox.setValue(argument);
			}
			//syncValueFunction();
		}
	}	

	private void updateValueFunctionSubPanels() {
		Function function = getSelectedFunction();
		if (function.getArguments().length != 0) {
			String argumentName = function.getArguments()[0];
			functionArgumentLabel.setText(argumentName);
		}
		setFunctionRowVisible(function.getArguments().length>0);
	}

	private void setFunctionRowVisible(boolean visible) {
		functionArgumentsRow.setVisible(visible);
	}

	private Function getSelectedFunction() {
		String selectedFunction = valueFunction.getValue(valueFunction.getSelectedIndex());
		return Function.valueOf(selectedFunction);
	}

	private void setSelectedFunction(Function function) {
		ListBoxUtils.selecteItem(valueFunction, function.toString());
	}

	private void setupAttributesPanel() {
		attributesPanel = new AttributesPanel(table, style.textboxError());
		attributesPanel.addValueChangeHandler(new ValueChangeHandler<Void>() {

			@Override
			public void onValueChange(ValueChangeEvent<Void> event) {
				fireChange();
			}
		});
	}

	public void setAttributes(List<UIAttribute> attributes) {
		Attributes.sortByAttributeType(attributes);
		attributesPanel.setAttributes(attributes);
	}

	public List<UIAttribute> getAttributes() {
		return attributesPanel.getAttributes();
	}

	public boolean areAttributesValid() {
		return attributesPanel.areValid();
	}


	public void setReadOnly(boolean readOnly) {
		nameBox.setEnabled(!readOnly);
		if (readOnly) nameBox.setStyleName(style.textboxError(), false);

		codelistBox.setEnabled(!readOnly);
		if (readOnly) codelistBox.setStyleName(style.textboxError(), false);

		valueTypePanel.setReadOnly(readOnly);

		valueFunction.setEnabled(!readOnly);

		functionArgumentBox.setEnabled(!readOnly);
		if (readOnly) functionArgumentBox.setStyleName(style.textboxError(), false);

		attributesPanel.setReadOnly(readOnly);
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
