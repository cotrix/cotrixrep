package org.cotrix.web.common.client.widgets.dialog;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(ProgressDialogImpl.class)
public interface ProgressDialog {
	
	
	public void showCentered();
	public void hide();
}