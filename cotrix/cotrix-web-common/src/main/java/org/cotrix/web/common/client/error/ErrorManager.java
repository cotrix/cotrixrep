/**
 * 
 */
package org.cotrix.web.common.client.error;

import org.cotrix.web.common.client.widgets.dialog.AlertDialog;
import org.cotrix.web.common.shared.Error;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ErrorManager {
	
	@Inject
	private AlertDialog alertDialog;
	
	public void showError(Error failureReport) {
		alertDialog.center(failureReport.getMessage(), failureReport.getDetails());
	}
}
