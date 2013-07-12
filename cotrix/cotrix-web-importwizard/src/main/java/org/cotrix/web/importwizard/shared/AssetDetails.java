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
public class AssetDetails implements Serializable {
	
	protected String id;
	protected String name;
	protected String type;
	protected List<Property> properties;
	protected RepositoryDetails repository;
	
	public AssetDetails(){}
	

	/**
	 * @param id
	 * @param name
	 * @param type
	 * @param properties
	 * @param repository
	 */
	public AssetDetails(String id, String name, String type,
			List<Property> properties, RepositoryDetails repository) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.properties = properties;
		this.repository = repository;
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
	 * @return the repository
	 */
	public RepositoryDetails getRepository() {
		return repository;
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
		builder.append(", repository=");
		builder.append(repository);
		builder.append("]");
		return builder.toString();
	}
}
