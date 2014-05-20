/**
 * 
 */
package org.cotrix.web.common.shared.codelist;

import java.io.Serializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial") 
public class Property implements Serializable {
	
	protected String name;
	protected String value;
	protected String description;
	
	public Property(){}
	
	/**
	 * @param name
	 * @param value
	 * @param description
	 */
	public Property(String name, String value, String description) {
		this.name = name;
		this.value = value;
		this.description = description;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Property [name=");
		builder.append(name);
		builder.append(", value=");
		builder.append(value);
		builder.append(", description=");
		builder.append(description);
		builder.append("]");
		return builder.toString();
	}
}
