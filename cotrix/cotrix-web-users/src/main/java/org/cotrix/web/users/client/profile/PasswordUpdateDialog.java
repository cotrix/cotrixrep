package org.cotrix.web.users.client.profile;

import com.google.inject.ImplementedBy;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(PasswordUpdateDialogImpl.class)
public interface PasswordUpdateDialog {
	
	public interface PasswordUpdateListener {
		public void onPasswordUpdate(String oldPassword, String newPassword);
	}

	public void setListener(PasswordUpdateListener listener);
	
	public void showCentered();

	public void clean();

}