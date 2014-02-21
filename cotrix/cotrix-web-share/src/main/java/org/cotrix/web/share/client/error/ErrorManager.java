/**
 * 
 */
package org.cotrix.web.share.client.error;

import org.cotrix.web.share.client.util.Exceptions;
import org.cotrix.web.share.client.widgets.AlertDialog;
import org.cotrix.web.share.shared.exception.ServiceException;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ErrorManager {

	public void rpcFailure(Throwable throwable) {
		String details = Exceptions.getPrintStackTrace(throwable);
		String errorMessage = getErrorMessage(throwable);
		AlertDialog.INSTANCE.center(errorMessage, details);
	}
	
	private String getErrorMessage(Throwable throwable) {
		if (throwable instanceof IncompatibleRemoteServiceException) return "Looks like you're running an old version of Cotrix, please <a href=\"http://en.wikipedia.org/wiki/Wikipedia:Bypass_your_cache\" target=\"_blank\">hard refresh</a> your browser";
		if (throwable instanceof InvocationException) return "Uhm, we cannot reach our servers. Check your connection or try again in a little bit.";
		if (throwable instanceof ServiceException) return "Ooops an error occurred on server side";
		
		return "Ooops an error occurred on server side";
	}
}
