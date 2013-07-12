/**
 * 
 */
package org.cotrix.web.importwizard.shared;

import java.io.Serializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AssetInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8076433432548925281L;
	
	protected String id;
	protected String name;
	protected String type;
	protected String repositoryName;
	
	public AssetInfo(){}
	
	/**
	 * @param id
	 * @param name
	 * @param type
	 * @param repositoryName
	 */
	public AssetInfo(String id, String name, String type, String repositoryName) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.repositoryName = repositoryName;
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
	 * @return the repositoryName
	 */
	public String getRepositoryName() {
		return repositoryName;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssetInfo [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", repositoryName=");
		builder.append(repositoryName);
		builder.append("]");
		return builder.toString();
	}

}
