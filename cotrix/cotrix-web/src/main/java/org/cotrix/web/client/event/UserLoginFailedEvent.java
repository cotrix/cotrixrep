package org.cotrix.web.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserLoginFailedEvent extends GenericEvent {

	protected String details;
	
	public UserLoginFailedEvent(String details) {
		this.details = details;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}
}
