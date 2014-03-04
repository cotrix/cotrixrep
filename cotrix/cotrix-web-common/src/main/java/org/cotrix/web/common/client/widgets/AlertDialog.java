package org.cotrix.web.common.client.widgets;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(AlertDialogImpl.class)
public interface AlertDialog {

	public void center(String message);

	public void center(String message, String details);

}