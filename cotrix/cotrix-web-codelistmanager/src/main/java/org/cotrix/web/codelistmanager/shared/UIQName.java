/**
 * 
 */
package org.cotrix.web.codelistmanager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UIQName implements IsSerializable {
	
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

}
