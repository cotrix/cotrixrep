package org.cotrix.web.client.userbar;

import org.cotrix.web.common.client.ext.HasExtensionArea;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(UserBarViewImpl.class)
public interface UserBarView extends HasExtensionArea {
	
	public static final String NAME = "UserBar";
	
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
	void setUserLoading(boolean loading);
}
