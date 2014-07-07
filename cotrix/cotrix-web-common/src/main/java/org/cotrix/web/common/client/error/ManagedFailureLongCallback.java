/**
 * 
 */
package org.cotrix.web.common.client.error;

import javax.inject.Inject;

import org.cotrix.web.common.client.widgets.dialog.LoaderDialog;
import org.cotrix.web.common.shared.Error;
import org.cotrix.web.common.shared.exception.Exceptions;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class ManagedFailureLongCallback<T> implements AsyncCallback<T> {
	
	@Inject
	private static ErrorManager errorManager;
	
	@Inject
	private static LoaderDialog progressDialog;

	public ManagedFailureLongCallback() {
		progressDialog.showCentered();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onSuccess(T result) {
		progressDialog.hide();
		onCallSuccess(result);
	}
	
	public abstract void onCallSuccess(T result);

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onFailure(Throwable caught) {
		progressDialog.hide();
		Error error = Exceptions.toError(caught);
		errorManager.showError(error);
		onCallFailed();
	}
	
	public void onCallFailed() {
	}

}
