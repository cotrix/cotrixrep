package org.cotrix.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserRegisteringEvent extends GwtEvent<UserRegisteringEvent.UserRegisteringEventHandler> {

	public static Type<UserRegisteringEventHandler> TYPE = new Type<UserRegisteringEventHandler>();

	public interface UserRegisteringEventHandler extends EventHandler {
		void onUserRegistering(UserRegisteringEvent event);
	}

	public UserRegisteringEvent() {
	}

	@Override
	protected void dispatch(UserRegisteringEventHandler handler) {
		handler.onUserRegistering(this);
	}

	@Override
	public Type<UserRegisteringEventHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserRegisteringEventHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new UserRegisteringEvent());
	}
}
