/**
 * 
 */
package org.cotrix.web.common.client.async;

import javax.inject.Inject;

import org.cotrix.web.common.client.error.ErrorManager;
import org.cotrix.web.common.client.widgets.dialog.LoaderDialog;
import org.cotrix.web.common.client.widgets.dialog.ProgressDialog;
import org.cotrix.web.common.client.widgets.dialog.ProgressDialog.ProgressCallBack;
import org.cotrix.web.common.shared.Error;
import org.cotrix.web.common.shared.LongTaskProgress;
import org.cotrix.web.common.shared.async.AsyncOutcome;
import org.cotrix.web.common.shared.async.AsyncOutput;
import org.cotrix.web.common.shared.async.AsyncTask;
import org.cotrix.web.common.shared.exception.Exceptions;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class AsyncUtils {

	@Inject
	private static LoaderDialog loaderDialog;

	@Inject
	private static ProgressDialog dialog;

	@Inject
	private static ErrorManager errorManager;

	public interface CancellableAsyncCallback<T> extends AsyncCallback<T> {
		public void onCancel();
	}

	public interface SuccessCallback<T>  {
		void onSuccess(T result);
	}

	public static <T extends IsSerializable> AsyncCallback<AsyncOutput<T>> async(final CancellableAsyncCallback<T> callback) {

		return new AsyncCallback<AsyncOutput<T>>() {

			@Override
			public void onSuccess(AsyncOutput<T> result) {
				if (!(result instanceof AsyncTask)) callback.onFailure(new IllegalStateException("Expected task not found"));
				else startProgress((AsyncTask<T>)result);
			}

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			private void startProgress(AsyncTask<T> task) {
				dialog.show(task.getId(), new ProgressCallBack() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(LongTaskProgress result) {
						AsyncOutcome<T> r = (AsyncOutcome<T>)result.getOutcome();
						callback.onSuccess(r.getOutcome());
					}

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onCancel() {
						callback.onCancel();						
					}
				});
			}
		};

	}

	public static <T extends IsSerializable> CancellableAsyncCallback<T> ignoreCancel(final AsyncCallback<T> callback) {
		return new CancellableAsyncCallback<T>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);				
			}

			@Override
			public void onSuccess(T result) {
				callback.onSuccess(result);
			}

			@Override
			public void onCancel() {
			}
		};
	}

	public static <T> AsyncCallback<T> manageAndReportError(final AsyncCallback<T> callback) {

		return new AsyncCallback<T>() {

			@Override
			public void onFailure(Throwable caught) {
				Error error = Exceptions.toError(caught);
				errorManager.showError(error);
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(T result) {
				callback.onSuccess(result);				
			}
		};

	}

	public static <T> AsyncCallback<T> manageError(final SuccessCallback<T> callback) {

		return new AsyncCallback<T>() {

			@Override
			public void onFailure(Throwable caught) {
				Error error = Exceptions.toError(caught);
				errorManager.showError(error);
			}

			@Override
			public void onSuccess(T result) {
				callback.onSuccess(result);				
			}
		};

	}


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
