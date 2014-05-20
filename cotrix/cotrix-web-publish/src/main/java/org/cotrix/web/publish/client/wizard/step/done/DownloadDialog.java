package org.cotrix.web.publish.client.wizard.step.done;

import com.google.inject.ImplementedBy;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@ImplementedBy(DownloadDialogImpl.class)
public interface DownloadDialog {
	
	public void showCentered(String ur);
	
	public void hide();


}