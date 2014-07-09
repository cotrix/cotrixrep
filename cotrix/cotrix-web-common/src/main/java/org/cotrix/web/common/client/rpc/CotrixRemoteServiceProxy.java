/**
 * 
 */
package org.cotrix.web.common.client.rpc;

import org.cotrix.web.common.client.widgets.dialog.ProgressDialog;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixRemoteServiceProxy extends RemoteServiceProxy {
	
	@Inject
	protected static CallBackListenerManager backListenerManager;
	
	@Inject
	protected static ProgressDialog progressDialog;

	protected CotrixRemoteServiceProxy(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			Serializer serializer) {
		super(moduleBaseURL, remoteServiceRelativePath, serializationPolicyName,
				serializer);
	}

	@Override
	protected <T> RequestCallback doCreateRequestCallback(RequestCallbackAdapter.ResponseReader responseReader,
			String methodName, RpcStatsContext statsContext,
			AsyncCallback<T> callback) {
		AsyncCallBackInterceptor<T> asyncCallBackInterceptor = new AsyncCallBackInterceptor<T>(callback, progressDialog);
		CallBackInterceptor<T> callBackInterceptor = new CallBackInterceptor<T>(backListenerManager, asyncCallBackInterceptor);
		return super.doCreateRequestCallback(responseReader, methodName, statsContext, callBackInterceptor);
	}

}
