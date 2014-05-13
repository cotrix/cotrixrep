/**
 * 
 */
package org.cotrix.web.ingest.shared;

import java.io.Serializable;
import java.util.List;

import org.cotrix.web.common.shared.codelist.Property;
import org.cotrix.web.common.shared.codelist.UIQName;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@SuppressWarnings("serial")
public class AssetDetails implements Serializable {
	
	protected String id;
	protected String name;
	protected String version;
	protected String type;
	protected List<Property> properties;
	protected UIQName repositoryName;
	protected UIQName repositoryId;
	
	public AssetDetails(){}

	public AssetDetails(String id, String name, String version, String type,
			List<Property> properties, UIQName repositoryName, UIQName repositoryId) {
		this.id = id;
		this.name = name;
		this.version = version;
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
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
	public UIQName getRepositoryName() {
		return repositoryName;
	}

	/**
	 * @return the repositoryId
	 */
	public UIQName getRepositoryId() {
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
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
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
	public void setRepositoryName(UIQName repositoryName) {
		this.repositoryName = repositoryName;
	}

	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(UIQName repositoryId) {
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
		builder.append(", version=");
		builder.append(version);
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
