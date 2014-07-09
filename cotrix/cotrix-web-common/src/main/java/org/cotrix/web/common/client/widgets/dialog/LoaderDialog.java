package org.cotrix.web.common.client.widgets.dialog;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(LoaderDialogImpl.class)
public interface LoaderDialog {
	public void showCentered();
	public void hide();
}