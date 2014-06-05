package org.cotrix.web.common.client.widgets.dialog;

import org.cotrix.web.common.shared.Error;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(AlertDialogImpl.class)
public interface AlertDialog {

	public void center(String message);

	public void center(String message, String details);
	
	public void center(Error error);

}