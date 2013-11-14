/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.io.Serializable;
import java.util.List;

import org.cotrix.web.share.shared.codelist.Property;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class AssetDetails implements Serializable {
	
	protected String id;
	protected String name;
	protected String type;
	protected List<Property> properties;
	protected String repositoryName;
	protected String repositoryId;
	
	public AssetDetails(){}
	

	/**
	 * @param id
	 * @param name
	 * @param type
	 * @param properties
	 * @param repository
	 */
	public AssetDetails(String id, String name, String type,
			List<Property> properties, String repositoryName, String repositoryId) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.properties = properties;
		this.repositoryName = repositoryName;
		this.repositoryId = repositoryId;

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the properties
	 */
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * @return the repositoryName
	 */
	public String getRepositoryName() {
		return repositoryName;
	}

	/**
	 * @return the repositoryId
	 */
	public String getRepositoryId() {
		return repositoryId;
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
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	/**
	 * @param repositoryName the repositoryName to set
	 */
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssetDetails [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", properties=");
		builder.append(properties);
		builder.append(", repositoryName=");
		builder.append(repositoryName);
		builder.append(", repositoryId=");
		builder.append(repositoryId);
		builder.append("]");
		return builder.toString();
	}
}
