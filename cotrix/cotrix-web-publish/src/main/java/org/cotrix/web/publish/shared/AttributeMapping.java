/**
 * 
 */
package org.cotrix.web.publish.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeMapping implements IsSerializable {
	
	protected AttributeDefinition attributeDefinition;
	protected String columnName;
	protected boolean mapped;
	
	/**
	 * @return the attributeDefinition
	 */
	public AttributeDefinition getAttributeDefinition() {
		return attributeDefinition;
	}
	/**
	 * @param attributeDefinition the attributeDefinition to set
	 */
	public void setAttributeDefinition(AttributeDefinition attributeDefinition) {
		this.attributeDefinition = attributeDefinition;
	}
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * @return the mapped
	 */
	public boolean isMapped() {
		return mapped;
	}
	
	/**
	 * @param mapped the mapped to set
	 */
	public void setMapped(boolean mapped) {
		this.mapped = mapped;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttributeMapping [attributeDefinition=");
		builder.append(attributeDefinition);
		builder.append(", columnName=");
		builder.append(columnName);
		builder.append(", mapped=");
		builder.append(mapped);
		builder.append("]");
		return builder.toString();
	}
}
