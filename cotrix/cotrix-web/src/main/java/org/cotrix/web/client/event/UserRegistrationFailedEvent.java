package org.cotrix.web.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserRegistrationFailedEvent extends GenericEvent {

	protected String message;
	protected String details;
	
	public UserRegistrationFailedEvent(String message, String details) {
		this.message = message;
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
