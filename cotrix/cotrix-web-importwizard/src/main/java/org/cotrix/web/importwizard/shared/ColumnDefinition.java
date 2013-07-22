/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ColumnDefinition implements IsSerializable {
	
	protected String name;
	protected ColumnType type;
	protected String language;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the type
	 */
	public ColumnType getType() {
		return type;
	}
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(ColumnType type) {
		this.type = type;
	}
	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ColumnDefinition [name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", language=");
		builder.append(language);
		builder.append("]");
		return builder.toString();
	}
}
