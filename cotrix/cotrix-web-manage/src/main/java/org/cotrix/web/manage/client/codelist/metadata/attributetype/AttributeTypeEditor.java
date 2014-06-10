/**
 * 
 */
package org.cotrix.web.manage.client.codelist.metadata.attributetype;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.attributetype.UIAttributeType;
import org.cotrix.web.common.shared.codelist.attributetype.UIConstraint;
import org.cotrix.web.common.shared.codelist.attributetype.UIRange;
import org.cotrix.web.manage.client.codelist.common.ItemPanel.ItemEditor;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeTypeEditor implements ItemEditor<UIAttributeType> {
	
	private AttributeTypeDetailsPanel detailsPanel;
	private UIAttributeType attributeType;
	
	public AttributeTypeEditor(UIAttributeType attributeType) {
		this.detailsPanel = new AttributeTypeDetailsPanel();
		this.attributeType = attributeType;
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
		String name = detailsPanel.getName();
		attributeType.setName(ValueUtils.getValue(name));
		
		String type = detailsPanel.getType();
		attributeType.setType(ValueUtils.getValue(type));
		
		Language language = detailsPanel.getLanguage();
		attributeType.setLanguage(language);
		
		String defaultValue = detailsPanel.getDefault();
		attributeType.setDefaultValue(defaultValue);
		
		UIRange range = detailsPanel.getRange();
		attributeType.setRange(range);
		
		List<UIConstraint> constraints = detailsPanel.getConstraints();
		attributeType.setConstraints(constraints);
	}

	@Override
	public void write() {
		detailsPanel.setName(ValueUtils.getLocalPart(attributeType.getName()));
		detailsPanel.setType(ValueUtils.getLocalPart(attributeType.getType()));
		detailsPanel.setLanguage(attributeType.getLanguage());
		detailsPanel.setDefault(attributeType.getDefaultValue());
		detailsPanel.setRange(attributeType.getRange());
		detailsPanel.setConstraints(attributeType.getConstraints());
	}

	@Override
	public String getLabel() {
		return ValueUtils.getLocalPart(attributeType.getName());
	}

	@Override
	public boolean validate() {
		boolean valid = true;

		String name = detailsPanel.getName();
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
	public UIAttributeType getItem() {
		return attributeType;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		detailsPanel.setReadOnly(readOnly);		
	}

	@Override
	public IsWidget getView() {
		return detailsPanel;
	}

	@Override
	public boolean isSwitchVisible() {
		return false;
	}

}
