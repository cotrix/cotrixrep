package org.cotrix.web.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserRegistrationFailedEvent extends GenericEvent {

	protected String details;
	
	public UserRegistrationFailedEvent(String details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}
}
