/**
 * 
 */
package org.cotrix.web.common.shared.codelist.linktype;

import java.util.List;

import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UIAttribute;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UILinkType implements Identifiable, IsSerializable {
	
	private String id;
	private UIQName name;
	private UICodelist targetCodelist;
	private UIValueFunction valueFunction;
	private UIValueType valueType;
	private List<UIAttribute> attributes;
	
	public UILinkType(){}
	
	public UILinkType(String id, UIQName name, UICodelist targetCodelist,
			UIValueFunction valueFunction, UIValueType valueType, List<UIAttribute> attributes) {
		this.id = id;
		this.name = name;
		this.targetCodelist = targetCodelist;
		this.valueFunction = valueFunction;
		this.valueType = valueType;
		this.attributes = attributes;
	}

	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(UIQName name) {
		this.name = name;
	}

	/**
	 * @return the targetCodelist
	 */
	public UICodelist getTargetCodelist() {
		return targetCodelist;
	}

	/**
	 * @param targetCodelist the targetCodelist to set
	 */
	public void setTargetCodelist(UICodelist targetCodelist) {
		this.targetCodelist = targetCodelist;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}	
	
	/**
	 * @return the valueFunction
	 */
	public UIValueFunction getValueFunction() {
		return valueFunction;
	}

	/**
	 * @param valueFunction the valueFunction to set
	 */
	public void setValueFunction(UIValueFunction valueFunction) {
		this.valueFunction = valueFunction;
	}

	/**
	 * @return the valueType
	 */
	public UIValueType getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(UIValueType valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the attributes
	 */
	public List<UIAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<UIAttribute> attributes) {
		this.attributes = attributes;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((targetCodelist == null) ? 0 : targetCodelist.hashCode());
		result = prime * result
				+ ((valueFunction == null) ? 0 : valueFunction.hashCode());
		result = prime * result
				+ ((valueType == null) ? 0 : valueType.hashCode());
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
		UILinkType other = (UILinkType) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (targetCodelist == null) {
			if (other.targetCodelist != null)
				return false;
		} else if (!targetCodelist.equals(other.targetCodelist))
			return false;
		if (valueFunction == null) {
			if (other.valueFunction != null)
				return false;
		} else if (!valueFunction.equals(other.valueFunction))
			return false;
		if (valueType == null) {
			if (other.valueType != null)
				return false;
		} else if (!valueType.equals(other.valueType))
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UILinkType [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", targetCodelist=");
		builder.append(targetCodelist);
		builder.append(", valueFunction=");
		builder.append(valueFunction);
		builder.append(", valueType=");
		builder.append(valueType);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append("]");
		return builder.toString();
	}



	public interface UIValueType {
	}
}
