/**
 * 
 */
package org.cotrix.web.share.shared.codelist;

import java.io.Serializable;
import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class RepositoryDetails implements Serializable {
	
	protected String id;
	protected String name;
	protected String publishedTypes;
	protected String returnedTypes;
	protected List<Property> properties;
	
	public RepositoryDetails(){}
	
	/**
	 * @param id
	 * @param name
	 * @param publishedTypes
	 * @param returnedTypes
	 * @param properties
	 */
	public RepositoryDetails(String id, String name, String publishedTypes,
			String returnedTypes, List<Property> properties) {
		this.id = id;
		this.name = name;
		this.publishedTypes = publishedTypes;
		this.returnedTypes = returnedTypes;
		this.properties = properties;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the publishedTypes
	 */
	public String getPublishedTypes() {
		return publishedTypes;
	}

	/**
	 * @return the returnedTypes
	 */
	public String getReturnedTypes() {
		return returnedTypes;
	}

	/**
	 * @return the properties
	 */
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param publishedTypes the publishedTypes to set
	 */
	public void setPublishedTypes(String publishedTypes) {
		this.publishedTypes = publishedTypes;
	}

	/**
	 * @param returnedTypes the returnedTypes to set
	 */
	public void setReturnedTypes(String returnedTypes) {
		this.returnedTypes = returnedTypes;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RepositoryDetails [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", publishedTypes=");
		builder.append(publishedTypes);
		builder.append(", returnedTypes=");
		builder.append(returnedTypes);
		builder.append(", properties=");
		builder.append(properties);
		builder.append("]");
		return builder.toString();
	}

}
