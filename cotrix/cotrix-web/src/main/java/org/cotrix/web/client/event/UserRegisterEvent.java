package org.cotrix.web.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UserRegisterEvent extends
		GwtEvent<UserRegisterEvent.UserRegisterHandler> {

	public static Type<UserRegisterHandler> TYPE = new Type<UserRegisterHandler>();
	private String username;
	private String password;
	private String email;

	public interface UserRegisterHandler extends EventHandler {
		void onUserRegister(UserRegisterEvent event);
	}

	public UserRegisterEvent(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	@Override
	protected void dispatch(UserRegisterHandler handler) {
		handler.onUserRegister(this);
	}

	@Override
	public Type<UserRegisterHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<UserRegisterHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String username, String password,
			String email) {
		source.fireEvent(new UserRegisterEvent(username, password, email));
	}
}
