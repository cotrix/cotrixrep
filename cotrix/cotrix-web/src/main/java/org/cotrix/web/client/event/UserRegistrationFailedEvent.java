package org.cotrix.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserRegistrationFailedEvent extends GwtEvent<UserRegistrationFailedEvent.UserRegistrationFailedEventHandler> {

	public static Type<UserRegistrationFailedEventHandler> TYPE = new Type<UserRegistrationFailedEventHandler>();

	public interface UserRegistrationFailedEventHandler extends EventHandler {
		void onUserRegistrationFailed(UserRegistrationFailedEvent event);
	}

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

	@Override
	protected void dispatch(UserRegistrationFailedEventHandler handler) {
		handler.onUserRegistrationFailed(this);
	}

	@Override
	public Type<UserRegistrationFailedEventHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserRegistrationFailedEventHandler> getType() {
		return TYPE;
	}
}
