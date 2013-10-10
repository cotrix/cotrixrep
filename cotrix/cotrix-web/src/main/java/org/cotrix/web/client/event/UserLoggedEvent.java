package org.cotrix.web.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserLoggedEvent extends GwtEvent<UserLoggedEvent.UserLoggedHandler> {

	public static Type<UserLoggedHandler> TYPE = new Type<UserLoggedHandler>();
	private String username;

	public interface UserLoggedHandler extends EventHandler {
		void onUserLogged(UserLoggedEvent event);
	}

	public UserLoggedEvent(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
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

	public static void fire(HasHandlers source, String username) {
		source.fireEvent(new UserLoggedEvent(username));
	}
}
