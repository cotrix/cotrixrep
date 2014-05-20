/**
 * 
 */
package org.cotrix.web.common.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Error implements IsSerializable {
	
	private String message;
	private String details;
	
	public Error(){}

	/**
	 * @param message
	 * @param details
	 */
	public Error(String message, String details) {
		this.message = message;
		this.details = details;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Error [message=");
		builder.append(message);
		builder.append(", details=");
		builder.append(details);
		builder.append("]");
		return builder.toString();
	}
}
