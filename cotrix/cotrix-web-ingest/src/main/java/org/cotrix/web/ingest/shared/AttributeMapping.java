/**
 * 
 */
package org.cotrix.web.ingest.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeMapping implements IsSerializable {
	
	protected Field field;
	protected AttributeDefinition attributeDefinition;
	
	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}
	
	/**
	 * @param field the field to set
	 */
	public void setField(Field field) {
		this.field = field;
	}
	
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
	
	public boolean isMapped()
	{
		return attributeDefinition!=null;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttributeMapping [field=");
		builder.append(field);
		builder.append(", attributeDefinition=");
		builder.append(attributeDefinition);
		builder.append("]");
		return builder.toString();
	}
}
