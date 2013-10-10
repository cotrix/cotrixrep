package org.cotrix.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserLogoutEvent extends GwtEvent<UserLogoutEvent.UserLogoutHandler> {

	public static Type<UserLogoutHandler> TYPE = new Type<UserLogoutHandler>();

	public interface UserLogoutHandler extends EventHandler {
		void onUserLogout(UserLogoutEvent event);
	}

	public UserLogoutEvent() {
	}

	@Override
	protected void dispatch(UserLogoutHandler handler) {
		handler.onUserLogout(this);
	}

	@Override
	public Type<UserLogoutHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserLogoutHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new UserLogoutEvent());
	}
}
