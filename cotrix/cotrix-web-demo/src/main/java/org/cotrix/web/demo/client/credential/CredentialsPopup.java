package org.cotrix.web.demo.client.credential;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(CredentialsPopupImpl.class)
public interface CredentialsPopup {
	
	public void showCentered();
	
	public void hide();
}