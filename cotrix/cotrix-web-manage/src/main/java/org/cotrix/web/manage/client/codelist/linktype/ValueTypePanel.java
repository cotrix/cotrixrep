/**
 * 
 */
package org.cotrix.web.manage.client.codelist.linktype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cotrix.web.common.client.util.ListBoxUtils;
import org.cotrix.web.common.shared.codelist.linktype.AttributeType;
import org.cotrix.web.common.shared.codelist.linktype.CodeNameType;
import org.cotrix.web.common.shared.codelist.linktype.LinkType;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	public static final CodeNameType CODE_NAME_TYPE = new CodeNameType();

	private static ValueTypePanelUiBinder uiBinder = GWT.create(ValueTypePanelUiBinder.class);

	interface ValueTypePanelUiBinder extends UiBinder<Widget, ValueTypePanel> {
	}
	
	@UiField EditableLabel valueTypeListContainer;
	@UiField ListBox valueTypeList;
	@UiField Image valueTypeListLoader;
	private Map<String, UIValueType> idToValueTypeMap;
	private Map<UIValueType, String> valueTypeToIdMap;
	
	private CodelistInfoProvider codelistInfoProvider;

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
	public void setCodelistInfoProvider(CodelistInfoProvider codelistInfoProvider) {
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
		valueTypeListLoader.setVisible(true);
		valueTypeListContainer.setVisible(false);
		
		codelistInfoProvider.getCodelistValueTypes(codelistId, new AsyncCallback<CodelistValueTypes>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(CodelistValueTypes result) {
				Log.trace("loaded valuetypes for codelist "+codelistId+":" + result);
				updateValueTypeList(result, selectedType);
				
				if (selectedType!=null) setValueType(selectedType);
				
				valueTypeListLoader.setVisible(false);
				valueTypeListContainer.setVisible(true);
			}
		});
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
		valueTypeList.clear();
		idToValueTypeMap.clear();
		
		addValueTypeCode();
		
		List<AttributeType> attributeTypes = codelistValueTypes.getAttributeTypes();
		if (selectedType!=null && selectedType instanceof AttributeType && !attributeTypes.contains(selectedType)) attributeTypes.add((AttributeType) selectedType);
		Map<AttributeType, String> attributeTypesLabels = ValueTypesGrouper.generateLabelsForAttributeTypes(attributeTypes);
		addValueTypeItems(attributeTypesLabels);
		
		List<LinkType> linkTypes = codelistValueTypes.getLinkTypes();
		if (selectedType!=null && selectedType instanceof LinkType && !linkTypes.contains(selectedType)) linkTypes.add((LinkType) selectedType);
		Map<LinkType, String> linkTypesLabels = ValueTypesGrouper.generateLabelsForLinkTypes(linkTypes);
		addValueTypeItems(linkTypesLabels);
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
