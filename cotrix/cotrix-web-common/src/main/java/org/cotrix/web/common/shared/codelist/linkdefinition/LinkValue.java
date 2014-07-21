/**
 * 
 */
package org.cotrix.web.common.shared.codelist.linkdefinition;

import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.codelist.linkdefinition.UILinkDefinition.UIValueType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkValue implements UIValueType, IsSerializable {
	
	private UIQName name;
	private String linkId;
	
	public LinkValue(){}

	public LinkValue(String linkId, UIQName name) {
		this.linkId = linkId;
		this.name = name;
	}


	/**
	 * @return the name
	 */
	public UIQName getName() {
		return name;
	}


	/**
	 * @return the linkId
	 */
	public String getLinkId() {
		return linkId;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linkId == null) ? 0 : linkId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		LinkValue other = (LinkValue) obj;
		if (linkId == null) {
			if (other.linkId != null)
				return false;
		} else if (!linkId.equals(other.linkId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LinkType [name=");
		builder.append(name);
		builder.append(", linkId=");
		builder.append(linkId);
		builder.append("]");
		return builder.toString();
	}
}
