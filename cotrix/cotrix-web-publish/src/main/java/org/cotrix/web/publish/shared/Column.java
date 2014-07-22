/**
 * 
 */
package org.cotrix.web.publish.shared;

import org.cotrix.web.publish.shared.DefinitionMapping.MappingTarget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Column implements MappingTarget {
	protected String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getLabel() {
		return name;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Column [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
	
}
