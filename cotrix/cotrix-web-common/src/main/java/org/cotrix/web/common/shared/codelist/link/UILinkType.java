/**
 * 
 */
package org.cotrix.web.common.shared.codelist.link;

import org.cotrix.web.common.shared.codelist.Identifiable;
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
	
	public UILinkType(){}
	
	public UILinkType(String id, UIQName name, UICodelist targetCodelist,
			UIValueFunction valueFunction, UIValueType valueType) {
		this.id = id;
		this.name = name;
		this.targetCodelist = targetCodelist;
		this.valueFunction = valueFunction;
		this.valueType = valueType;
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

	public interface UIValueType {
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		builder.append("]");
		return builder.toString();
	}
}
