/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.util.ListBoxUtils;
import org.cotrix.web.common.client.widgets.EditableLabel;
import org.cotrix.web.common.shared.codelist.linktype.AttributeValue;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameValue;
import org.cotrix.web.common.shared.codelist.linktype.LinkValue;
import org.cotrix.web.common.shared.codelist.linktype.UILinkType.UIValueType;
import org.cotrix.web.manage.client.util.ValueTypesGrouper;
import org.cotrix.web.manage.shared.CodelistValueTypes;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ValueTypePanel extends Composite implements HasValueChangeHandlers<UIValueType> {
	
	public static final String CODE_NAME_VALUE_TYPE = Document.get().createUniqueId();
	public static final CodeNameValue CODE_NAME_TYPE = new CodeNameValue();

	private static ValueTypePanelUiBinder uiBinder = GWT.create(ValueTypePanelUiBinder.class);

	interface ValueTypePanelUiBinder extends UiBinder<Widget, ValueTypePanel> {
	}
	
	@UiField EditableLabel valueTypeListContainer;
	@UiField ListBox valueTypeList;
	@UiField Image valueTypeListLoader;
	private Map<String, UIValueType> idToValueTypeMap;
	private Map<UIValueType, String> valueTypeToIdMap;
	
	private LinkTypesCodelistInfoProvider codelistInfoProvider;

	public ValueTypePanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		idToValueTypeMap = new HashMap<String, UIValueType>();
		valueTypeToIdMap = new HashMap<UIValueType, String>();
		addValueTypeCode();
		
		valueTypeList.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				syncValueTypeContainerText();
				fireChange();
			}
		});
	}
	
	/**
	 * @param codelistInfoProvider the codelistInfoProvider to set
	 */
	public void setCodelistInfoProvider(LinkTypesCodelistInfoProvider codelistInfoProvider) {
		this.codelistInfoProvider = codelistInfoProvider;
	}

	public void setValueType(UIValueType type) {
		Log.trace("setValueType type: "+type);
		String id = valueTypeToIdMap.get(type);
		ListBoxUtils.selecteItem(valueTypeList, id);
		syncValueTypeContainerText();
	}
	
	public UIValueType getValueType() {
		String id = valueTypeList.getValue(valueTypeList.getSelectedIndex());
		return idToValueTypeMap.get(id);
	}
	
	public void setCodelist(final String codelistId, final UIValueType selectedType) {
		if (codelistId!=null) loadCodelist(codelistId, selectedType);
		else clear();
	}
	
	private void loadCodelist(final String codelistId, final UIValueType selectedType) {
		showLoader(true);
		
		codelistInfoProvider.getCodelistValueTypes(codelistId, new ManagedFailureCallback<CodelistValueTypes>() {

			@Override
			public void onSuccess(CodelistValueTypes result) {
				Log.trace("loaded valuetypes for codelist "+codelistId+":" + result);
				updateValueTypeList(result, selectedType);
				
				if (selectedType!=null) setValueType(selectedType);
				
				showLoader(false);
			}
		});
	}
	
	private void showLoader(boolean show) {
		valueTypeListLoader.setVisible(show);
		valueTypeListContainer.setVisible(!show);
	}
	
	private void mapValueType(String id, UIValueType type) {
		idToValueTypeMap.put(id, type);
		valueTypeToIdMap.put(type, id);
	}
	
	private void addValueTypeCode() {
		valueTypeList.addItem("Code", CODE_NAME_VALUE_TYPE);
		mapValueType(CODE_NAME_VALUE_TYPE, CODE_NAME_TYPE);
		ListBoxUtils.setItemColor(valueTypeList, CODE_NAME_VALUE_TYPE, "black");

		valueTypeList.setSelectedIndex(0);
		syncValueTypeContainerText();
	}
	
	private void updateValueTypeList(CodelistValueTypes codelistValueTypes, UIValueType selectedType) {
		clear();
		
		addValueTypeCode();
		
		List<AttributeValue> attributeTypes = codelistValueTypes.getAttributeTypes();
		if (selectedType!=null && selectedType instanceof AttributeValue && !attributeTypes.contains(selectedType)) attributeTypes.add((AttributeValue) selectedType);
		Map<AttributeValue, String> attributeTypesLabels = ValueTypesGrouper.generateLabelsForAttributeTypes(attributeTypes);
		addValueTypeItems(attributeTypesLabels);
		
		List<LinkValue> linkTypes = codelistValueTypes.getLinkTypes();
		if (selectedType!=null && selectedType instanceof LinkValue && !linkTypes.contains(selectedType)) linkTypes.add((LinkValue) selectedType);
		Map<LinkValue, String> linkTypesLabels = ValueTypesGrouper.generateLabelsForLinkTypes(linkTypes);
		addValueTypeItems(linkTypesLabels);
	}
	
	private void clear() {
		valueTypeList.clear();
		idToValueTypeMap.clear();
	}
	
	private void addValueTypeItems(Map<? extends UIValueType, String> labels) {
		for (Entry<? extends UIValueType, String> entry:labels.entrySet()) {
			UIValueType type = entry.getKey();
			String label = entry.getValue();
			String id = Document.get().createUniqueId();
			valueTypeList.addItem(label, id);
			mapValueType(id, type);
		}
	}
	
	private void syncValueTypeContainerText() {
		valueTypeListContainer.setText(valueTypeList.getItemText(valueTypeList.getSelectedIndex()));
	}

	private void fireChange() {
		ValueChangeEvent.fire(this, getValueType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<UIValueType> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void setReadOnly(boolean readOnly) {
		valueTypeListContainer.setReadOnly(readOnly);
	}
	

}
