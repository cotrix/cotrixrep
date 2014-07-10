/**
 * 
 */
package org.cotrix.web.common.client.async;

import org.cotrix.web.common.shared.async.AsyncOutcome;
import org.cotrix.web.common.shared.async.AsyncOutput;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AsyncUtils {
	
	public static <T extends IsSerializable> AsyncCallback<AsyncOutput<T>> async(final AsyncCallback<T> callback) {
		return new AsyncCallback<AsyncOutput<T>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);				
			}

			@Override
			public void onSuccess(AsyncOutput<T> result) {
				if (!(result instanceof AsyncOutcome)) {
					callback.onFailure(new IllegalStateException("Expected outcome not found"));
				} else {
					callback.onSuccess(((AsyncOutcome<T>)result).getOutcome());
				}
			}
		};
	}

}
