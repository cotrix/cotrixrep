/**
 * 
 */
package org.cotrix.web.common.client.error;

import javax.inject.Inject;

import org.cotrix.web.common.shared.Error;
import org.cotrix.web.common.shared.exception.Exceptions;

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
		Error error = Exceptions.toError(caught);
		errorManager.showError(error);
	}

}
