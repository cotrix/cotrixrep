/**
 * 
 */
package org.cotrix.web.share.client.error;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class IgnoreFailureCallback<T> implements AsyncCallback<T> {

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onFailure(Throwable caught) {
		Log.trace("RPC onFailure intentionally ignored: "+caught.getMessage());
	}

}
