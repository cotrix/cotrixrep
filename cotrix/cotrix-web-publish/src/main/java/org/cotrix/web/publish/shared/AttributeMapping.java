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
	
	public interface Mapping extends IsSerializable {
		public String getLabel();
	}
	
	protected AttributeDefinition attributeDefinition;
	protected Mapping mapping;
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
	 * @return the mapping
	 */
	public Mapping getMapping() {
		return mapping;
	}
	/**
	 * @param mapping the mapping to set
	 */
	public void setMapping(Mapping mapping) {
		this.mapping = mapping;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttributeMapping [attributeDefinition=");
		builder.append(attributeDefinition);
		builder.append(", mapping=");
		builder.append(mapping);
		builder.append(", mapped=");
		builder.append(mapped);
		builder.append("]");
		return builder.toString();
	}
}
