package org.cotrix.web.client.event;

import com.google.web.bindery.event.shared.binder.GenericEvent;
import org.cotrix.web.common.shared.Error;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserLoginFailedEvent extends GenericEvent {

	protected Error error;
	
	public UserLoginFailedEvent(Error error) {
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public Error getError() {
		return error;
	}
}
