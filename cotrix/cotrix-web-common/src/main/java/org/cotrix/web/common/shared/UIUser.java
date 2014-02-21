/**
 * 
 */
package org.cotrix.web.common.shared;

import org.cotrix.web.common.shared.feature.FeatureCarrier;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIUser extends FeatureCarrier {
	
	protected String id;
	protected String username;
	protected String fullName;
	
	public UIUser(){}
	
	/**
	 * @param id
	 * @param username
	 */
	public UIUser(String id, String username, String fullName) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
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
		UIUser other = (UIUser) obj;
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
		builder.append("UIUser [id=");
		builder.append(id);
		builder.append(", username=");
		builder.append(username);
		builder.append(", fullName=");
		builder.append(fullName);
		builder.append("]");
		return builder.toString();
	}
}
