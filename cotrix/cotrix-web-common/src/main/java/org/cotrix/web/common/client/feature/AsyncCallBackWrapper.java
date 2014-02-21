/**
 * 
 */
package org.cotrix.web.common.client.feature;

import org.cotrix.web.common.shared.feature.ResponseWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AsyncCallBackWrapper<T> implements AsyncCallback<ResponseWrapper<T>> {
	
	public static <T> AsyncCallBackWrapper<T> wrap(AsyncCallback<T> wrapped)
	{
		return new AsyncCallBackWrapper<T>(wrapped);
	}
	
	protected AsyncCallback<T> wrapped;

	/**
	 * @param wrapped
	 */
	public AsyncCallBackWrapper(AsyncCallback<T> wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void onFailure(Throwable caught) {
		wrapped.onFailure(caught);		
	}

	@Override
	public void onSuccess(ResponseWrapper<T> result) {
		wrapped.onSuccess(result.getPayload());
	}
	
	

}
