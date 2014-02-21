/**
 * 
 */
package org.cotrix.web.share.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class Nop<T> implements AsyncCallback<T> {
	
	protected static Nop<Object> INSTANCE = new Nop<Object>();
	
	@SuppressWarnings("unchecked")
	public static <T> Nop<T> getInstance()
	{
		return (Nop<T>) INSTANCE;
	}

	@Override
	public void onFailure(Throwable caught) {
	}

	@Override
	public void onSuccess(T result) {
	}

}
