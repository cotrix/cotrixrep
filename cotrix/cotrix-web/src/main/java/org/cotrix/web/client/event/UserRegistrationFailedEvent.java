package org.cotrix.web.client.event;

import org.cotrix.web.common.shared.Error;

import com.google.web.bindery.event.shared.binder.GenericEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserRegistrationFailedEvent extends GenericEvent {

	protected Error error;
	
	public UserRegistrationFailedEvent(Error error) {
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public Error getError() {
		return error;
	}
}
