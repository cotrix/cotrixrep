/**
 * 
 */
package org.cotrix.web.ingest.shared;

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
	
	public static final String NAME_FIELD = "NAME";
	public static final String REPOSITORY_FIELD = "REPOSITORY";
	
	protected String id;
	protected String name;
	protected String type;
	protected CodeListType codeListType;
	protected String repositoryId;
	protected String repositoryName;
	
	public AssetInfo(){}

	/**
	 * @param id
	 * @param name
	 * @param type
	 * @param repositoryId
	 * @param repositoryName
	 */
	public AssetInfo(String id, String name, String type, CodeListType codeListType, String repositoryId,
			String repositoryName) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.codeListType = codeListType;
		this.repositoryId = repositoryId;
		this.repositoryName = repositoryName;
	}

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
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the codeListType
	 */
	public CodeListType getCodeListType() {
		return codeListType;
	}

	/**
	 * @param codeListType the codeListType to set
	 */
	public void setCodeListType(CodeListType codeListType) {
		this.codeListType = codeListType;
	}

	/**
	 * @return the repositoryId
	 */
	public String getRepositoryId() {
		return repositoryId;
	}

	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	/**
	 * @return the repositoryName
	 */
	public String getRepositoryName() {
		return repositoryName;
	}

	/**
	 * @param repositoryName the repositoryName to set
	 */
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
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
		builder.append(", codeListType=");
		builder.append(codeListType);
		builder.append(", repositoryId=");
		builder.append(repositoryId);
		builder.append(", repositoryName=");
		builder.append(repositoryName);
		builder.append("]");
		return builder.toString();
	}	
}
