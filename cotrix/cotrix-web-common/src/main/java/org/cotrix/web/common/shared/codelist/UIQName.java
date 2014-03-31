/**
 * 
 */
package org.cotrix.web.common.shared.codelist;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIQName implements IsSerializable, Comparable<UIQName>, Cloneable {
	
	protected String namespace;
	protected String localPart;
	
	protected UIQName(){}
	
	/**
	 * @param namespace
	 * @param localPart
	 */
	public UIQName(String namespace, String localPart) {
		this.namespace = namespace;
		this.localPart = localPart;
	}

	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @return the localPart
	 */
	public String getLocalPart() {
		return localPart;
	}

	/**
	 * @param namespace the namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @param localPart the localPart to set
	 */
	public void setLocalPart(String localPart) {
		this.localPart = localPart;
	}
	
	public String toHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		if (namespace!=null && !namespace.isEmpty()) htmlBuilder.append(namespace).append(" ");
		if (localPart!=null) htmlBuilder.append(localPart);
		return htmlBuilder.toString();
	}
	
	public UIQName clone()
	{
		return new UIQName(namespace, localPart);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localPart == null) ? 0 : localPart.hashCode());
		result = prime * result
				+ ((namespace == null) ? 0 : namespace.hashCode());
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
		UIQName other = (UIQName) obj;
		if (localPart == null) {
			if (other.localPart != null)
				return false;
		} else if (!localPart.equals(other.localPart))
			return false;
		if (namespace == null) {
			if (other.namespace != null)
				return false;
		} else if (!namespace.equals(other.namespace))
			return false;
		return true;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UIQName [namespace=");
		builder.append(namespace);
		builder.append(", localPart=");
		builder.append(localPart);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(UIQName o) {
		int compare = (namespace !=null)?namespace.compareTo(o.namespace):1;
		if (compare!=0) return compare;

		return (localPart !=null)?localPart.compareTo(o.localPart):1;
	}

}
