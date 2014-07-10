/**
 * 
 */
package org.cotrix.web.common.client.rpc;

import org.cotrix.web.common.client.widgets.dialog.ProgressDialog;
import org.cotrix.web.common.client.widgets.dialog.ProgressDialog.ProgressCallBack;
import org.cotrix.web.common.shared.LongTaskProgress;
import org.cotrix.web.common.shared.async.AsyncTask;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AsyncCallBackInterceptor<T> implements AsyncCallback<T> {

	private AsyncCallback<T> delegate;
	private ProgressDialog dialog;

	public AsyncCallBackInterceptor(AsyncCallback<T> delegate, ProgressDialog dialog) {
		this.delegate = delegate;
		this.dialog = dialog;
	}

	@Override
	public void onSuccess(T result) {
		if (result!=null && result.getClass().equals(AsyncTask.class)) {
			startProgress((AsyncTask<?>)result);
		} else delegate.onSuccess(result);
	}

	@Override
	public void onFailure(Throwable caught) {
		delegate.onFailure(caught);
	}
	
	private void startProgress(AsyncTask<?> task) {
		dialog.show(task.getId(), new ProgressCallBack() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(LongTaskProgress result) {
				T r = (T)result.getOutcome();
				delegate.onSuccess(r);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				delegate.onFailure(caught);
			}
		});
	}

}
