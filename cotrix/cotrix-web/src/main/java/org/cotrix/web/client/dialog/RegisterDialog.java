package org.cotrix.web.client.dialog;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(RegisterDialogImpl.class)
public interface RegisterDialog {
	
	public interface RegisterDialogListener {
		public void onRegister(String username, String password, String email);
	}

	public void setListener(RegisterDialogListener listener);
	public RegisterDialogListener getListener();
	
	public void showCentered();
	public void hide();

	public void clean();

	

}