/**
 * 
 */
package org.cotrix.web.common.client.async;

import javax.inject.Inject;

import org.cotrix.web.common.client.widgets.dialog.LoaderDialog;
import org.cotrix.web.common.shared.async.AsyncOutcome;
import org.cotrix.web.common.shared.async.AsyncOutput;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AsyncUtils {
	
	@Inject
	private static LoaderDialog loaderDialog;
	
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
	
	public static <T> AsyncCallback<T> showLoader(final AsyncCallback<T> callback) {
		loaderDialog.showCentered();
		return new AsyncCallback<T>() {

			@Override
			public void onFailure(Throwable caught) {
				loaderDialog.hide();
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(T result) {
				loaderDialog.hide();
				callback.onSuccess(result);
			}
		};
	}
}
