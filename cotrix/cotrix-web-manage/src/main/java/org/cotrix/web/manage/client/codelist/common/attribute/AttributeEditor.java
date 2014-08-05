/**
 * 
 */
package org.cotrix.web.manage.client.codelist.common.attribute;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.manage.client.codelist.cache.AttributeDefinitionsCache;
import org.cotrix.web.manage.client.codelist.common.form.ItemPanel.ItemEditor;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeEditor implements ItemEditor<UIAttribute> {
	
	private UIAttribute attribute;
	private AttributeDetailsPanel detailsPanel;
	
	//FIXME
//			if (Attributes.isSystemAttribute(attribute)) header.addHeaderStyle(CotrixManagerResources.INSTANCE.css().systemAttributeDisclosurePanelLabel());


	public AttributeEditor(UIAttribute attribute, boolean hasDefinition, AttributeDescriptionSuggestOracle oracle, AttributeDefinitionsCache attributeTypesCache) {
		this.attribute = attribute;
		detailsPanel = new AttributeDetailsPanel(oracle, attributeTypesCache);
		detailsPanel.setDefinitionVisible(hasDefinition);
	}
	
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return detailsPanel.addValueChangeHandler(handler);
	}

	@Override
	public void startEditing() {
		detailsPanel.setReadOnly(false);
	}

	@Override
	public void stopEditing() {
		detailsPanel.setReadOnly(true);
		
	}

	@Override
	public void onEdit(AsyncCallback<Boolean> callBack) {
		callBack.onSuccess(true);
	}

	@Override
	public void read() {
		UIAttributeDefinition definition = detailsPanel.getDefinition();
		attribute.setDefinitionId(definition==null?null:definition.getId());
		
		UIQName name = detailsPanel.getName();
		attribute.setName(name);
		
		String type = detailsPanel.getType();
		String typeNamespace = ValueUtils.defaultNamespace;
		UIQName oldType = attribute.getType();
		//we preserve the namespace
		if (oldType!=null && oldType.getLocalPart().equals(type)) typeNamespace = oldType.getNamespace();
		attribute.setType(new UIQName(typeNamespace, type));
		
		String description = detailsPanel.getDescription();
		attribute.setDescription(description);
		
		Language language = detailsPanel.getLanguage();
		attribute.setLanguage(language);
		
		String value = detailsPanel.getValue();
		attribute.setValue(value);
	}

	@Override
	public void write() {
		detailsPanel.setDefinitionId(attribute.getDefinitionId());
		detailsPanel.setName(attribute.getName());
		detailsPanel.setType(ValueUtils.getLocalPart(attribute.getType()));
		detailsPanel.setDescription(attribute.getDescription());
		detailsPanel.setLanguage(attribute.getLanguage());
		detailsPanel.setValue(attribute.getValue());
		
	}

	@Override
	public String getLabel() {
		return ValueUtils.getLocalPart(attribute.getName());
	}

	@Override
	public String getLabelValue() {
		String valueLabel = attribute.getValue()!=null?attribute.getValue():"n/a";
		return ": "+valueLabel;
		//FIXME
//		header.setHeaderValueVisible(!disclosurePanel.isOpen() && attribute.getValue()!=null);

	}

	@Override
	public boolean validate() {
		boolean valid = true;

		Log.trace("validating");
		
		UIQName name = detailsPanel.getName();
		Log.trace("name: "+name);
		
		boolean nameValid = name!=null && !name.isEmpty();
		Log.trace("nameValid: "+nameValid);
		detailsPanel.setNameFieldValid(nameValid);
		valid &= nameValid;
		
		UIAttributeDefinition definition = detailsPanel.getDefinition();
		if (definition!=null) {
			boolean valueValid = evaluate(definition.getExpression(), detailsPanel.getValue());
			detailsPanel.setValueFieldValid(valueValid, "Be warned, the current value does not satisfy all the schema constraints for "+name.getLocalPart().toUpperCase());
		}

		return valid;
	}
	
	private boolean evaluate(String expression, String value) {
		String filledExpression = expression.replaceAll("\\$value", "\""+value+"\"");
		Log.trace("evaluating "+filledExpression);
		return eval(filledExpression);
	}
	
	private native boolean eval(String expression) /*-{
		return $wnd.eval(expression);
	}-*/;

	@Override
	public UIAttribute getItem() {
		return attribute;
	}

	@Override
	public IsWidget getView() {
		return detailsPanel;
	}

	@Override
	public boolean isSwitchVisible() {
		return true;
	}

	@Override
	public ImageResource getBullet() {
		return null;
	}

	@Override
	public void onSave() {
		
	}

}
