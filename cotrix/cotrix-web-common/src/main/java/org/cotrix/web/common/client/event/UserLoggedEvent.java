package org.cotrix.web.common.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import org.cotrix.web.common.shared.UIUser;

import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserLoggedEvent extends GwtEvent<UserLoggedEvent.UserLoggedHandler> {

	public static Type<UserLoggedHandler> TYPE = new Type<UserLoggedHandler>();
	private UIUser user;

	public interface UserLoggedHandler extends EventHandler {
		void onUserLogged(UserLoggedEvent event);
	}

	public UserLoggedEvent(UIUser user) {
		this.user = user;
	}

	public UIUser getUser() {
		return user;
	}

	@Override
	protected void dispatch(UserLoggedHandler handler) {
		handler.onUserLogged(this);
	}

	@Override
	public Type<UserLoggedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserLoggedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, UIUser user) {
		source.fireEvent(new UserLoggedEvent(user));
	}
}
