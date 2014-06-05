package org.cotrix.web.common.client.widgets.dialog;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(ConfirmDialogImpl.class)
public interface ConfirmDialog {
	
	public enum DialogButton {CONTINUE, CANCEL};
	
	public interface ConfirmDialogListener {
		public void onButtonClick(DialogButton button);
	}

	public void center(String message, ConfirmDialogListener listener, DialogButton ... disabledButtons);
	
	public void warning(String message);

}