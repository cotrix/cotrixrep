/**
 * 
 */
package org.cotrix.web.share.client.error;

import org.cotrix.web.share.client.util.Exceptions;
import org.cotrix.web.share.client.widgets.AlertDialog;

import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ErrorManager {

	public void rpcFailure(Throwable throwable) {
		String details = Exceptions.getPrintStackTrace(throwable);
		AlertDialog.INSTANCE.center("Ooops an error occurred on server side", details);
	}



}
