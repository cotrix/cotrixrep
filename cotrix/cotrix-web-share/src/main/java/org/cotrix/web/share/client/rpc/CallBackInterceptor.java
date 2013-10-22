/**
 * 
 */
package org.cotrix.web.share.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CallBackInterceptor<T> implements AsyncCallback<T> {

	protected CallBackListenerManager listenerManager;
	protected AsyncCallback<T> delegate;

	/**
	 * @param delegate
	 */
	public CallBackInterceptor(CallBackListenerManager listenerManager, AsyncCallback<T> delegate) {
		this.listenerManager = listenerManager;
		this.delegate = delegate;
	}

	@Override
	public void onSuccess(T result) {
		boolean propagate = listenerManager.onSuccess(result);
		if (propagate) delegate.onSuccess(result);
	}

	@Override
	public void onFailure(Throwable caught) {
		boolean propagate = listenerManager.onFailure(caught);
		if (propagate) delegate.onFailure(caught);
	}

}
