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
public class UILink implements Identifiable, IsSerializable, HasAttributes, HasValue {
	
	private String id;
	
	private String typeId;
	private UIQName typeName;
	
	private String targetId;
	private UIQName targetName;
	
	private String value;
	
	private List<UIAttribute> attributes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public UIQName getTypeName() {
		return typeName;
	}

	public void setTypeName(UIQName typeName) {
		this.typeName = typeName;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public UIQName getTargetName() {
		return targetName;
	}

	public void setTargetName(UIQName targetName) {
		this.targetName = targetName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<UIAttribute> getAttributes() {
		return attributes;
	}

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