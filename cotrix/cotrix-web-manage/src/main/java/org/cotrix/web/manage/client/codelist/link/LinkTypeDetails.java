/**
 * 
 */
package org.cotrix.web.manage.client.codelist.link;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.link.UILinkType.UIValueType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkTypeDetails {
	
	private String name;
	private UICodelist codelist;
	private UIValueType valueType;
	private String function;
	private String customFunction;
	
	/**
	 * @param name
	 * @param codelist
	 * @param valueType
	 * @param attribute
	 * @param function
	 * @param customFunction
	 */
	public LinkTypeDetails(String name, UICodelist codelist,
			UIValueType valueType, String function,
			String customFunction) {
		this.name = name;
		this.codelist = codelist;
		this.valueType = valueType;
		this.function = function;
		this.customFunction = customFunction;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the codelist
	 */
	public UICodelist getCodelist() {
		return codelist;
	}
	/**
	 * @return the valueType
	 */
	public UIValueType getValueType() {
		return valueType;
	}

	/**
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @param function the function to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param codelist the codelist to set
	 */
	public void setCodelist(UICodelist codelist) {
		this.codelist = codelist;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(UIValueType valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the customFunction
	 */
	public String getCustomFunction() {
		return customFunction;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codelist == null) ? 0 : codelist.hashCode());
		result = prime * result
				+ ((customFunction == null) ? 0 : customFunction.hashCode());
		result = prime * result
				+ ((function == null) ? 0 : function.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		LinkTypeDetails other = (LinkTypeDetails) obj;
		if (codelist == null) {
			if (other.codelist != null)
				return false;
		} else if (!codelist.equals(other.codelist))
			return false;
		if (customFunction == null) {
			if (other.customFunction != null)
				return false;
		} else if (!customFunction.equals(other.customFunction))
			return false;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (valueType != other.valueType)
			return false;
		return true;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LinkTypeDetail [name=");
		builder.append(name);
		builder.append(", codelist=");
		builder.append(codelist);
		builder.append(", valueType=");
		builder.append(valueType);
		builder.append(", function=");
		builder.append(function);
		builder.append(", customFunction=");
		builder.append(customFunction);
		builder.append("]");
		return builder.toString();
	}
}
