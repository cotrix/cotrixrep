/**
 * 
 */
package org.cotrix.web.share.client.event;

import org.cotrix.web.share.client.feature.FeatureBus;
import org.cotrix.web.share.client.feature.NewFeatureSetEvent;
import org.cotrix.web.share.shared.feature.Response;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FeatureAsyncCallBack<T> implements AsyncCallback<Response<T>> {
	
	@Inject @FeatureBus
	protected static EventBus featureBus;
	
	public static <T> AsyncCallback<Response<T>> wrap(AsyncCallback<T> callback)
	{
		return new FeatureAsyncCallBack<T>(callback);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> AsyncCallback<Response<T>> nop()
	{
		return new FeatureAsyncCallBack<T>((AsyncCallback<T>) NOP);
	}
	
	protected static AsyncCallback<Object> NOP = new AsyncCallback<Object>() {

		@Override
		public void onFailure(Throwable caught) {}

		@Override
		public void onSuccess(Object result) {}
	};

	protected AsyncCallback<T> innerCallBack;

	/**
	 * @param innerCallBack
	 */
	public FeatureAsyncCallBack(AsyncCallback<T> innerCallBack) {
		this.innerCallBack = innerCallBack;
	}

	@Override
	public void onSuccess(Response<T> result) {
		if (result.getApplicationFeatures()!=null) featureBus.fireEvent(new NewFeatureSetEvent(result.getApplicationFeatures(), result.getCodelistsFeatures()));
		else Log.warn("No features in response");
		
		innerCallBack.onSuccess(result.getPayload());
	}

	@Override
	public void onFailure(Throwable caught) {
		innerCallBack.onFailure(caught);
	}


}
