/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributedefinition;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIConstraint;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIRange;
import org.cotrix.web.manage.client.codelist.common.ItemPanel.ItemEditor;
import org.cotrix.web.manage.client.codelist.common.attribute.AttributeDescriptionSuggestOracle;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinitionEditor implements ItemEditor<UIAttributeDefinition> {
	
	private AttributeDefinitionDetailsPanel detailsPanel;
	private UIAttributeDefinition attributeDefinition;
	
	private boolean editing = false;
	
	public AttributeDefinitionEditor(UIAttributeDefinition attributeDefinition, AttributeDescriptionSuggestOracle attributeDescriptionSuggestOracle) {
		this.detailsPanel = new AttributeDefinitionDetailsPanel(attributeDescriptionSuggestOracle);
		this.attributeDefinition = attributeDefinition;
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Void> handler) {
		return detailsPanel.addValueChangeHandler(handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		detailsPanel.fireEvent(event);		
	}

	@Override
	public void read() {
		UIQName name = detailsPanel.getName();
		attributeDefinition.setName(name);
		
		String type = detailsPanel.getType();
		UIQName oldType = attributeDefinition.getType();
		String namespace = ValueUtils.defaultNamespace;
		if (oldType!=null && oldType.getLocalPart().equals(type)) namespace = oldType.getNamespace();
		attributeDefinition.setType(new UIQName(namespace, type));
		
		Language language = detailsPanel.getLanguage();
		attributeDefinition.setLanguage(language);
		
		String defaultValue = detailsPanel.getDefault();
		attributeDefinition.setDefaultValue(defaultValue);
		
		UIRange range = detailsPanel.getRange();
		attributeDefinition.setRange(range);
		
		List<UIConstraint> constraints = detailsPanel.getConstraints();
		attributeDefinition.setConstraints(constraints);
	}

	@Override
	public void write() {
		detailsPanel.setName(attributeDefinition.getName());
		detailsPanel.setType(ValueUtils.getLocalPart(attributeDefinition.getType()));
		detailsPanel.setLanguage(attributeDefinition.getLanguage());
		detailsPanel.setDefault(attributeDefinition.getDefaultValue());
		detailsPanel.setRange(attributeDefinition.getRange());
		detailsPanel.setConstraints(attributeDefinition.getConstraints());
	}

	@Override
	public String getLabel() {
		if (!editing) return ValueUtils.getLocalPart(attributeDefinition.getName());
		
		UIQName name = detailsPanel.getName();
		return name.isEmpty()?"...":name.getLocalPart();
	}

	@Override
	public boolean validate() {
		boolean valid = true;

		UIQName name = detailsPanel.getName();
		boolean nameValid = name!=null && !name.isEmpty();
		detailsPanel.setNameFieldValid(nameValid);
		valid &= nameValid;
		
		UIRange range = detailsPanel.getRange();
		boolean rangeValid = range.getMin()!=Integer.MIN_VALUE && range.getMax()!=Integer.MIN_VALUE;
		detailsPanel.setRangeFieldValid(rangeValid);
		valid &= rangeValid;
		
		return valid;
	}

	@Override
	public UIAttributeDefinition getItem() {
		return attributeDefinition;
	}

	@Override
	public IsWidget getView() {
		return detailsPanel;
	}

	@Override
	public boolean isSwitchVisible() {
		return false;
	}

	@Override
	public void startEditing() {
		detailsPanel.setReadOnly(false);
		detailsPanel.focusName();
		editing = true;
	}

	@Override
	public void stopEditing() {
		detailsPanel.setReadOnly(true);
		editing = false;
	}

	@Override
	public String getLabelValue() {
		return "";
	}

	@Override
	public ImageResource getBullet() {
		return null;
	}

}
