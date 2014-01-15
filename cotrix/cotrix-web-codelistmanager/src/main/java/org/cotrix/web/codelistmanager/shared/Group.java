/**
 * 
 */
package org.cotrix.web.codelistmanager.shared;

import java.util.List;

import org.cotrix.web.share.shared.codelist.UIAttribute;
import org.cotrix.web.share.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Group implements Cloneable, Comparable<Group>, IsSerializable {
	
	protected UIQName name;
	protected UIQName type;
	protected String language;
	protected int position;
	
	protected boolean isSystemGroup;
	
	protected String label;
	
	protected Group() {
		
	}
		
	/**
	 * @param name
	 * @param type
	 * @param language
	 */
	public Group(UIQName name, UIQName type, String language, boolean isSystemGroup) {
		this.name = name!=null?name.clone():null;
		this.type = type!=null?type.clone():null;
		this.language = language;
		this.isSystemGroup = isSystemGroup;
		createLabel();
	}

	protected void createLabel()
	{
		StringBuilder labelBuilder = new StringBuilder();
		if (name!=null) labelBuilder.append(name.getLocalPart());
		if (type!=null) {
			if (labelBuilder.length() != 0) labelBuilder.append(" ");
			labelBuilder.append(type.getLocalPart());
		}
		if (language!=null && !language.isEmpty()) {
			if (labelBuilder.length() != 0) labelBuilder.append(" ");
			labelBuilder.append('(').append(language).append(')');
		}
		
		this.label = labelBuilder.toString(); 
	}
	
	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public UIQName getType() {
		return type;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	public String getLabel()
	{
		return label;
	}
	
	public boolean isSystemGroup() {
		return isSystemGroup;
	}
	
	public void calculatePosition(List<UIAttribute> attributes, UIAttribute attribute)
	{
		int position = getPosition(attributes, attribute);
		setPosition(position);
	}
	
	public void setPosition(int position)
	{
		this.position = position;
		createLabel();
	}

	public int getPosition() {
		return position;
	}

	protected int getPosition(List<UIAttribute> attributes, UIAttribute att)
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
	
	public String getValue(List<UIAttribute> attributes)
	{
		UIAttribute attribute = match(attributes);
		return attribute!=null?attribute.getValue():"";
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
	
	protected boolean accept(UIAttribute attribute)
	{
		if (name!=null && !name.equals(attribute.getName())) return false;
		if (type!=null && !type.equals(attribute.getType())) return false;
		if (language!=null && !language.equals(attribute.getLanguage())) return false;
		return true;
	}
	
	public Group clone()
	{
		Group clone = new Group(name, type, language, isSystemGroup);
		clone.setPosition(position);
		return clone;
	}


	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isSystemGroup ? 1231 : 1237);
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + position;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (isSystemGroup != other.isSystemGroup)
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (position != other.position)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Group [name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", language=");
		builder.append(language);
		builder.append(", position=");
		builder.append(position);
		builder.append(", isSystemGroup=");
		builder.append(isSystemGroup);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Group o) {
		int compare = (name !=null)?name.compareTo(o.name):1;
		if (compare!=0) return compare;
		
		compare = (type !=null)?type.compareTo(o.type):1;
		if (compare!=0) return compare;
		
		compare = (language !=null)?language.compareTo(o.language):1;
		if (compare!=0) return compare;
		
		compare = position > o.position ? +1 : position < o.position ? -1 : 0;
		if (compare!=0) return compare;
		
		return isSystemGroup && !o.isSystemGroup ? +1 : !isSystemGroup && o.isSystemGroup ? -1 : 0;
	}

}