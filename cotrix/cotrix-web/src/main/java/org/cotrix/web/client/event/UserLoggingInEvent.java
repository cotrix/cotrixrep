package org.cotrix.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserLoggingInEvent extends GwtEvent<UserLoggingInEvent.UserLoggingInEventHandler> {

	public static Type<UserLoggingInEventHandler> TYPE = new Type<UserLoggingInEventHandler>();

	public interface UserLoggingInEventHandler extends EventHandler {
		void onUserLoggingIn(UserLoggingInEvent event);
	}

	public UserLoggingInEvent() {
	}

	@Override
	protected void dispatch(UserLoggingInEventHandler handler) {
		handler.onUserLoggingIn(this);
	}

	@Override
	public Type<UserLoggingInEventHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserLoggingInEventHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new UserLoggingInEvent());
	}
}
