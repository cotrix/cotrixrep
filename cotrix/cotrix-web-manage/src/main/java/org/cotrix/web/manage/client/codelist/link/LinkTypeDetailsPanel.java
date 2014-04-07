/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.link.AttributeType;
import org.cotrix.web.common.shared.codelist.link.CodeNameType;
import org.cotrix.web.common.shared.codelist.link.LinkType;
import org.cotrix.web.common.shared.codelist.link.UILinkType;
import org.cotrix.web.common.shared.codelist.link.UILinkType.UIValueType;
import org.cotrix.web.manage.client.codelist.link.CodelistSuggestOracle.CodelistSuggestion;
import org.cotrix.web.manage.client.resources.CotrixManagerResources;
import org.cotrix.web.manage.client.util.AttributeTypes;
import org.cotrix.web.manage.client.util.ValueTypesGrouper;
import org.cotrix.web.manage.shared.CodelistValueTypes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
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
public class LinkTypeDetailsPanel extends Composite implements HasValueChangeHandlers<UILinkType>{
	
	public static final String NONE_FUNCTION = "NONE";
	public static final String OTHER_FUNCTION = "OTHER";
	
	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameType CODE_NAME_TYPE = new CodeNameType();

	private static LinkTypeDetailsPanelUiBinder uiBinder = GWT.create(LinkTypeDetailsPanelUiBinder.class);

	interface LinkTypeDetailsPanelUiBinder extends UiBinder<Widget, LinkTypeDetailsPanel> {}
	
	interface Style extends CssResource {
		String error();
		String editor();
	}
	
	@UiField EditableLabel nameContainer;
	@UiField TextBox name;
	
	@UiField EditableLabel codelistContainer;
	@UiField(provided=true) SuggestBox codelist;
	@UiField Image codelistLoader;

	@UiField EditableLabel valueTypeContainer;
	@UiField ListBox valueType;
	@UiField Image valueTypeLoader;
	private Map<String, UIValueType> idToValueTypeMap;
	private Map<UIValueType, String> valueTypeToIdMap;
	
	@UiField EditableLabel valueFunctionContainer;
	@UiField ListBox valueFunction;
	
	@UiField TableRowElement functionRow;
	@UiField EditableLabel functionContainer;
	@UiField TextBox function;
	
	
	@UiField Style style;
	
	private UICodelist selectedCodelist;
	
	private CodelistSuggestOracle codelistSuggestOracle = new CodelistSuggestOracle();	
	
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
				updateValueType(selectedCodelist.getId(), null);
				codelistContainer.setText(CodelistSuggestion.toDisplayString(selectedCodelist));
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
		
		idToValueTypeMap = new HashMap<String, UIValueType>();
		valueTypeToIdMap = new HashMap<UIValueType, String>();
		addValueTypeCode();
		
		valueType.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				syncValueTypeContainerText();
				fireChange();
			}
		});
		
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
	
	private UIValueType getValueType() {
		String id = valueType.getValue(valueType.getSelectedIndex());
		return idToValueTypeMap.get(id);
	}
	
	private void updateValueFunctionSubPanels() {
		String function = valueFunction.getValue(valueFunction.getSelectedIndex());
		setFunctionBoxVisible(OTHER_FUNCTION.equals(function));
	}
	
	private void setFunctionBoxVisible(boolean visible) {
		UIObject.setVisible(functionRow, visible);
	}
	
	private String getValueFunction() {
		String selectedFunction = valueFunction.getValue(valueFunction.getSelectedIndex());
		if (NONE_FUNCTION.equals(selectedFunction)) return null;
		if (OTHER_FUNCTION.equals(selectedFunction)) return OTHER_FUNCTION;
		return selectedFunction;
	}
	
	private void updateValueType(final String codelistId, final UIValueType type) {
		valueTypeLoader.setVisible(true);
		valueTypeContainer.setVisible(false);
		
		codelistInfoProvider.getCodelistValueTypes(codelistId, new AsyncCallback<CodelistValueTypes>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(CodelistValueTypes result) {
				
				//FIXME add type to list of values
				updateValueTypeList(result);
				if (type!=null) setValueType(type);
				
				valueTypeLoader.setVisible(false);
				valueTypeContainer.setVisible(true);
			}
		});
	}
	
	private void mapValueType(String id, UIValueType type) {
		idToValueTypeMap.put(id, type);
		valueTypeToIdMap.put(type, id);
	}
	
	private void addValueTypeCode() {
		valueType.addItem("Code", CODE_NAME_VALUE_TYPE);
		mapValueType(CODE_NAME_VALUE_TYPE, CODE_NAME_TYPE);
		setItemColor(valueType, CODE_NAME_VALUE_TYPE, "black");

		valueType.setSelectedIndex(0);
		syncValueTypeContainerText();
	}
	
	private void updateValueTypeList(CodelistValueTypes codelistValueTypes) {
		valueType.clear();
		idToValueTypeMap.clear();
		
		addValueTypeCode();
		
		Map<AttributeType, String> attributeTypesLabels = ValueTypesGrouper.generateLabelsForAttributeTypes(codelistValueTypes.getAttributeTypes());
		addValueTypeItems(attributeTypesLabels);
		
		Map<LinkType, String> linkTypesLabels = ValueTypesGrouper.generateLabelsForLinkTypes(codelistValueTypes.getLinkTypes());
		addValueTypeItems(linkTypesLabels);
	}
	
	private void addValueTypeItems(Map<? extends UIValueType, String> labels) {
		for (Entry<? extends UIValueType, String> entry:labels.entrySet()) {
			UIValueType type = entry.getKey();
			String label = entry.getValue();
			String id = Document.get().createUniqueId();
			valueType.addItem(label, id);
			mapValueType(id, type);
		}
	}
	
	private void setItemColor(ListBox listBox, String itemValue, String color) {
		NodeList<Node> children = listBox.getElement().getChildNodes();       
	    for (int i = 0; i< children.getLength();i++) {
	      Node child = children.getItem(i);
	        if (child.getNodeType()==Node.ELEMENT_NODE) {
	          if (child instanceof OptionElement) {
	            OptionElement optionElement = (OptionElement) child;
	              if (optionElement.getValue().equals(itemValue)) {
	                 optionElement.getStyle().setColor(color);  
	              }                   
	            }
	          }           
	       }
	}
	
	public void setValidName(boolean valid) {
		System.out.println("setValidName valid: "+valid);
		name.setStyleName(style.error(), !valid);
	}
	
	public void setValidCodelist(boolean valid) {
		System.out.println("setValidCodelist valid: "+valid);
		codelist.setStyleName(style.error(), !valid);
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
		
		valueFunctionContainer.setReadOnly(readOnly);
		
		functionContainer.setReadOnly(readOnly);
		if (readOnly) function.setStyleName(style.error(), false);
	}
	
	public void clear() {
		name.setValue("", false);
		codelist.getValueBox().setValue("", false);
		selectedCodelist = null;
		valueFunction.setItemSelected(0, true);
		function.setValue("", false);
	}
	
	public void setLinkType(UILinkType details) {
		name.setValue(ValueUtils.getLocalPart(details.getName()), false);
		nameContainer.setText(ValueUtils.getLocalPart(details.getName()));
		
		selectedCodelist = details.getTargetCodelist();
		codelist.getValueBox().setValue(CodelistSuggestion.toDisplayString(selectedCodelist), false);
		codelistContainer.setText(CodelistSuggestion.toDisplayString(selectedCodelist));
		
		updateValueType(selectedCodelist.getId(), details.getValueType());

		setValueFunction(details.getValueFunction());
		valueFunctionContainer.setText(details.getValueFunction());
		
		function.setValue(details.getValueFunction(), false);
		functionContainer.setText(details.getValueFunction());
	}
	
	private void setValueType(UIValueType type) {
		String id = valueTypeToIdMap.get(type);
		selecteItem(valueType, id);
		syncValueTypeContainerText();
	}
	
	private void syncValueTypeContainerText() {
		valueTypeContainer.setText(valueType.getItemText(valueType.getSelectedIndex()));
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

	public UILinkType getLinkType() {
		
		String customFunction = function.isVisible()?null:function.getValue();
		return new UILinkType("", ValueUtils.getValue(name.getValue()), selectedCodelist, getValueFunction(), getValueType());
	}
	
	private void fireChange() {
		ValueChangeEvent.fire(this, getLinkType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<UILinkType> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

}
