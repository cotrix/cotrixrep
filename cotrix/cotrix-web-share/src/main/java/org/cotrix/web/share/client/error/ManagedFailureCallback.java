/**
 * 
 */
package org.cotrix.web.share.client.error;

import javax.inject.Inject;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class ManagedFailureCallback<T> implements AsyncCallback<T> {
	
	@Inject
	protected static ErrorManager errorManager;

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onFailure(Throwable caught) {
		errorManager.rpcFailure(caught);
	}

}
