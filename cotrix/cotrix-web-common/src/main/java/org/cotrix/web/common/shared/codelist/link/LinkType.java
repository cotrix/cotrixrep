/**
 * 
 */
package org.cotrix.web.common.shared.codelist.link;

import org.cotrix.web.common.shared.codelist.link.UILinkType.UIValueType;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class LinkType implements UIValueType, IsSerializable {
	
	private UILinkType linkType;
	
	public LinkType(){}

	/**
	 * @param linkType
	 */
	public LinkType(UILinkType linkType) {
		this.linkType = linkType;
	}

	/**
	 * @return the linkType
	 */
	public UILinkType getLinkType() {
		return linkType;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((linkType == null) ? 0 : linkType.hashCode());
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
		LinkType other = (LinkType) obj;
		if (linkType == null) {
			if (other.linkType != null)
				return false;
		} else if (!linkType.equals(other.linkType))
			return false;
		return true;
	}



	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LinkType [linkType=");
		builder.append(linkType);
		builder.append("]");
		return builder.toString();
	}
}
