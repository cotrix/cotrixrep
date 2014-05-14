/**
 * 
 */
package org.cotrix.web.publish.shared;

import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AttributeDefinition implements IsSerializable {
	
	protected UIQName name;
	protected UIQName type;
	protected Language language;
	
	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}
	/**
	 * @return the type
	 */
	public UIQName getType() {
		return type;
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
	public void setName(UIQName name) {
		this.name = name;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(UIQName type) {
		this.type = type;
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
