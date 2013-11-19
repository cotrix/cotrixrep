/**
 * 
 */
package org.cotrix.web.share.client.error;

import javax.inject.Inject;

import org.cotrix.web.share.client.rpc.CallBackListener;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CallbackFailureListener implements CallBackListener {

	@Inject
	protected static ErrorManager errorManager;
	
	@Override
	public boolean onFailure(Throwable caught) {
		errorManager.rpcFailure(caught);
		return false;
	}

	@Override
	public boolean onSuccess(Object result) {
		return true;
	}

}
