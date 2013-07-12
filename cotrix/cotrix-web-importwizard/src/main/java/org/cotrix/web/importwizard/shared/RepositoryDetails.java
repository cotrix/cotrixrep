/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.io.Serializable;
import java.util.List;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class RepositoryDetails implements Serializable {
	
	protected String name;
	protected String publishedTypes;
	protected String returnedTypes;
	protected List<Property> properties;
	
	public RepositoryDetails(){}
	
	/**
	 * @param name
	 * @param publishedTypes
	 * @param returnedTypes
	 * @param properties
	 */
	public RepositoryDetails(String name, String publishedTypes,
			String returnedTypes, List<Property> properties) {
		this.name = name;
		this.publishedTypes = publishedTypes;
		this.returnedTypes = returnedTypes;
		this.properties = properties;
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RepositoryDetails [name=");
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
