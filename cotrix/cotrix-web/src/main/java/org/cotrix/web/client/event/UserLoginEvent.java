package org.cotrix.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserLoginEvent extends GwtEvent<UserLoginEvent.UserLoginHandler> {

	public static Type<UserLoginHandler> TYPE = new Type<UserLoginHandler>();
	private String username;
	private String password;

	public interface UserLoginHandler extends EventHandler {
		void onUserLogin(UserLoginEvent event);
	}

	public UserLoginEvent(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	protected void dispatch(UserLoginHandler handler) {
		handler.onUserLogin(this);
	}

	@Override
	public Type<UserLoginHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserLoginHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String username, String password) {
		source.fireEvent(new UserLoginEvent(username, password));
	}
}
