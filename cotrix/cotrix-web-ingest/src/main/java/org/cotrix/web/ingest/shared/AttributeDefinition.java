/**
 * 
 */
package org.cotrix.web.ingest.shared;

import org.cotrix.web.common.shared.Language;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinition implements IsSerializable {
	
	private String name;
	private AttributeType type;
	private String customType;
	private Language language;
	private boolean optional;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the type
	 */
	public AttributeType getType() {
		return type;
	}
	/**
	 * @return the customType
	 */
	public String getCustomType() {
		return customType;
	}
	/**
	 * @param customType the customType to set
	 */
	public void setCustomType(String customType) {
		this.customType = customType;
	}
	/**
	 * @return the language
	 */
	public Language getLanguage() {
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
	public void setType(AttributeType type) {
		this.type = type;
	}
	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}
	/**
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}
	/**
	 * @param optional the optional to set
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttributeDefinition [name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", customType=");
		builder.append(customType);
		builder.append(", language=");
		builder.append(language);
		builder.append(", optional=");
		builder.append(optional);
		builder.append("]");
		return builder.toString();
	}
}
