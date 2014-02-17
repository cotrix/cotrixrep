package org.cotrix.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserLoginFailedEvent extends GwtEvent<UserLoginFailedEvent.UserLoginFailedEventHandler> {

	public static Type<UserLoginFailedEventHandler> TYPE = new Type<UserLoginFailedEventHandler>();

	public interface UserLoginFailedEventHandler extends EventHandler {
		void onUserLoginFailed(UserLoginFailedEvent event);
	}

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

	@Override
	protected void dispatch(UserLoginFailedEventHandler handler) {
		handler.onUserLoginFailed(this);
	}

	@Override
	public Type<UserLoginFailedEventHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserLoginFailedEventHandler> getType() {
		return TYPE;
	}
}
