/**
 * 
 */
package org.cotrix.web.common.shared.codelist.attributetype;

import org.cotrix.web.common.shared.Language;
import org.cotrix.web.common.shared.codelist.Identifiable;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIAttributeType implements IsSerializable, Identifiable {
	
	private String id;
	private UIQName name;
	private UIQName type;
	private Language language;
	
	public UIAttributeType() {}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIAttributeType [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", language=");
		builder.append(language);
		builder.append("]");
		return builder.toString();
	}	
}
