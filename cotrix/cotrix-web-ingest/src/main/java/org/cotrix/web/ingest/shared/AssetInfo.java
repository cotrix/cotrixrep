/**
 * 
 */
package org.cotrix.web.ingest.shared;

import java.io.Serializable;

import org.cotrix.web.common.shared.codelist.UIQName;

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
	public static final String VERSION_FIELD = "VERSION";
	public static final String REPOSITORY_FIELD = "REPOSITORY";
	
	private String id;
	private String name;
	private String version;
	private String type;
	private CodeListType codeListType;
	private UIQName repositoryId;
	private UIQName repositoryName;
	
	public AssetInfo(){}

	public AssetInfo(String id, String name, String version, String type, CodeListType codeListType, UIQName repositoryId,
			UIQName repositoryName) {
		this.id = id;
		this.name = name;
		this.version = version;
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
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
	public UIQName getRepositoryId() {
		return repositoryId;
	}

	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(UIQName repositoryId) {
		this.repositoryId = repositoryId;
	}

	/**
	 * @return the repositoryName
	 */
	public UIQName getRepositoryName() {
		return repositoryName;
	}

	/**
	 * @param repositoryName the repositoryName to set
	 */
	public void setRepositoryName(UIQName repositoryName) {
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
		builder.append(", version=");
		builder.append(version);
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
