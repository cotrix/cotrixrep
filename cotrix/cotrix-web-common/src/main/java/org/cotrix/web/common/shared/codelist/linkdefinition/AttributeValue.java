/**
 * 
 */
package org.cotrix.web.common.shared.codelist.linkdefinition;

import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition.UIValueType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeValue implements UIValueType, IsSerializable {
	
	private UIQName name;
	private UIQName type;
	private Language language;
	
	public AttributeValue(){}

	/**
	 * @param name
	 * @param type
	 * @param language
	 */
	public AttributeValue(UIQName name, UIQName type, Language language) {
		this.name = name;
		this.type = type;
		this.language = language;
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
	 * @return the type
	 */
	public UIQName getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(UIQName type) {
		this.type = type;
	}

	/**
	 * @return the language
	 */
	public Language getLanguage() {
		return language;
	}
	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		AttributeValue other = (AttributeValue) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AttributeType [name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", language=");
		builder.append(language);
		builder.append("]");
		return builder.toString();
	}
}
