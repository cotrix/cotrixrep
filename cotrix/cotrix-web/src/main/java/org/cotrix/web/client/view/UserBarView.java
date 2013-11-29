package org.cotrix.web.client.view;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public interface UserBarView {
	
	public interface Presenter {
		public void onUserClick();
		public void onLoginClick();
		public void onLogoutClick();
		public void onRegisterClick();
	}
	
	public void setUsername(String username);
	public void setLoginVisible(boolean visible);
	public void setLogoutVisible(boolean visible);

	public void setPresenter(Presenter presenter);
	Widget asWidget();
	void setStatus(String status);
	void setUserEnabled(boolean enabled);
	void setRegisterVisible(boolean visible);
	void setUsernameClickEnabled(boolean enabled);
}
