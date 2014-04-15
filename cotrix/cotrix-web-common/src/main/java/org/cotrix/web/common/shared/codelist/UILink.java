/**
 * 
 */
package org.cotrix.web.common.shared.codelist;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UILink implements Identifiable, IsSerializable, HasAttributes {
	
	private String id;
	
	private String typeId;
	private String typeName;
	
	private String targetId;
	private String targetName;
	
	private String value;
	
	private List<UIAttribute> attributes;

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
	 * @return the typeId
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the targetId
	 */
	public String getTargetId() {
		return targetId;
	}

	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @param targetName the targetName to set
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the attributes
	 */
	public List<UIAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(List<UIAttribute> attributes) {
		this.attributes = attributes;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UILink other = (UILink) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UILink [id=");
		builder.append(id);
		builder.append(", typeId=");
		builder.append(typeId);
		builder.append(", typeName=");
		builder.append(typeName);
		builder.append(", targetId=");
		builder.append(targetId);
		builder.append(", targetName=");
		builder.append(targetName);
		builder.append(", value=");
		builder.append(value);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append("]");
		return builder.toString();
	}
}
