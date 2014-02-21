/**
 * 
 */
package org.cotrix.web.common.client.error;

import org.cotrix.web.common.client.rpc.CallBackListener;

import com.allen_sauer.gwt.log.client.Log;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CallbackFailureLogger implements CallBackListener {
	
	@Override
	public boolean onFailure(Throwable caught) {
		Log.error("catched failure:", caught);
		return true;
	}

	@Override
	public boolean onSuccess(Object result) {
		return true;
	}

}
