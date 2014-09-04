/**
 * 
 */
package org.cotrix.web.manage.shared;

import java.util.List;

import org.cotrix.web.common.client.util.ValueUtils;
import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICode;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.attributedefinition.UIAttributeDefinition;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeGroup implements Comparable<AttributeGroup>, Group, HasPosition {
	
	private UIAttributeDefinition definition;
	private int position;
	
	private SafeHtml label;
	
	protected AttributeGroup() {
	}
		
	public AttributeGroup(UIAttributeDefinition definition) {
		this.definition = definition;
	}

	public UIQName getName() {
		return definition.getName();
	}
	
	public Language getLanguage() {
		return definition.getLanguage();
	}
	
	public void calculatePosition(List<UIAttribute> attributes, UIAttribute attribute) {
		int position = getPosition(attributes, attribute);
		setPosition(position);
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	@Override
	public int getPosition() {
		return position;
	}

	private int getPosition(List<UIAttribute> attributes, UIAttribute att)
	{
		int index = 0;
		for (UIAttribute attribute:attributes) {
			if (accept(attribute)) {
				if (att.equals(attribute)) return index;
				index++;
			}
		}
		return -1;
	}
	
	public boolean accept(List<UIAttribute> attributes, UIAttribute att)
	{
		int index = 0;
		for (UIAttribute attribute:attributes) {
			if (accept(attribute)) {
				if (att.equals(attribute) && index == position) return true;
				index++;
			}
			if (index>position) return false;
		}
		return false;
	}
	
	public UIAttribute match(List<UIAttribute> attributes)
	{
		int index = 0;
		for (UIAttribute attribute:attributes) {
			if (accept(attribute)) {
				if (index == position) return attribute;
				index++;
			}
		}
		return null;
	}
	
	private boolean accept(UIAttribute attribute)
	{
		return attribute.getDefinitionId()!=null && attribute.getDefinitionId().equals(definition.getId());
	}
	
	public AttributeGroup cloneGroup()
	{
		AttributeGroup clone = new AttributeGroup(definition);
		clone.setPosition(position);
		return clone;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public CodelistEditorSortInfo getSortInfo(boolean ascending) {
		return new CodelistEditorSortInfo.AttributeGroupSortInfo(ascending, definition.getName(), definition.getType(), definition.getLanguage(), position);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public SafeHtml getLabel() {
		if (label == null) buildLabel();
		return label;
	}
	
	@Override
	public boolean isEditable() {
		return true;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getValue(UICode code)
	{
		List<UIAttribute> attributes = code.getAttributes();
		UIAttribute attribute = match(attributes);
		return attribute!=null?attribute.getValue():"";
	}
	
	private void buildLabel() {
		SafeHtmlBuilder labelBuilder = new SafeHtmlBuilder();
		labelBuilder.appendHtmlConstant("<span style=\"vertical-align:middle;padding-right: 7px;\">");
		SafeHtml nameHtml = SafeHtmlUtils.fromString(ValueUtils.getValue(definition.getName()));
		labelBuilder.append(nameHtml);
		labelBuilder.appendHtmlConstant("</span>");
		if (definition.getLanguage()!=Language.NONE) {
			labelBuilder.appendHtmlConstant("<span style=\"vertical-align:middle;color:black;padding-left:5px;\" " +
					"title=\""+definition.getLanguage().getName()+"\">(");
			labelBuilder.append(SafeHtmlUtils.fromString(definition.getLanguage().getCode()));
			labelBuilder.appendHtmlConstant(")</span>");
		}
		
		label = labelBuilder.toSafeHtml();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((definition == null) ? 0 : definition.hashCode());
		result = prime * result + position;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeGroup other = (AttributeGroup) obj;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		if (position != other.position)
			return false;
		return true;
	}

	@Override
	public int compareTo(AttributeGroup o) {
		
		UIAttributeDefinition otherDefinition = o.definition;
		
		int compare = (definition.getName() !=null)?definition.getName().compareTo(otherDefinition.getName()):1;
		if (compare!=0) return compare;
		
		compare = (definition.getType() !=null)?definition.getType().compareTo(otherDefinition.getType()):1;
		if (compare!=0) return compare;
		
		compare = (definition.getLanguage() !=null)?definition.getLanguage().compareTo(otherDefinition.getLanguage()):1;
		if (compare!=0) return compare;
		
		compare = position > o.position ? +1 : position < o.position ? -1 : 0;
		return compare;
	}

	@Override
	public boolean isSortable() {
		return true;
	}

	@Override
	public String getSubtitle() {
		StringBuilder subtitle = new StringBuilder();
		
		if (definition.getType()!=null) subtitle.append(definition.getType().getLocalPart());
		
		if (definition.getLanguage()!=Language.NONE) {
			if (definition.getType()!=null) subtitle.append(", ");
			subtitle.append(definition.getLanguage().getName());
		}
		
		return subtitle.toString();
	}

}
