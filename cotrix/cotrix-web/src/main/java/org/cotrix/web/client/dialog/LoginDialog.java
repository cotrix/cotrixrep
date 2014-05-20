package org.cotrix.web.client.dialog;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(LoginDialogImpl.class)
public interface LoginDialog {
	
	public interface LoginDialogListener {
		public void onLogin(String username, String password);
		public void onRegister();
	}

	public void setListener(LoginDialogListener listener);
	public LoginDialogListener getListener();
	
	public void showCentered();
	
	public void hide();

	public void clean();

}