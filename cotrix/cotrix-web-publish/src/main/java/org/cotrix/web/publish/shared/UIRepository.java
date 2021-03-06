/**
 * 
 */
package org.cotrix.web.publish.shared;

import java.util.List;

import org.cotrix.web.common.shared.Format;
import org.cotrix.web.common.shared.codelist.UIQName;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIRepository implements IsSerializable {
	
	public static final String NAME_FIELD = "NAME";
	public static final String PUBLISHED_TYPES_FIELD = "PUBLISHED_TYPES";
	
	protected UIQName id;
	protected UIQName name;
	protected String publishedTypes;
	protected List<Format> availableFormats;
	
	/**
	 * @return the id
	 */
	public UIQName getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(UIQName id) {
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
	 * @return the publishedTypes
	 */
	public String getPublishedTypes() {
		return publishedTypes;
	}
	/**
	 * @param publishedTypes the publishedTypes to set
	 */
	public void setPublishedTypes(String publishedTypes) {
		this.publishedTypes = publishedTypes;
	}
	
	/**
	 * @return the availableFormats
	 */
	public List<Format> getAvailableFormats() {
		return availableFormats;
	}
	
	/**
	 * @param availableFormats the availableFormats to set
	 */
	public void setAvailableFormats(List<Format> availableFormats) {
		this.availableFormats = availableFormats;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIRepository [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", publishedTypes=");
		builder.append(publishedTypes);
		builder.append(", availableFormats=");
		builder.append(availableFormats);
		builder.append("]");
		return builder.toString();
	}
}
