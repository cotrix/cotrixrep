/**
 * 
 */
package org.cotrix.web.share.client.event;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.share.client.feature.FeatureBus;
import org.cotrix.web.share.client.feature.NewFeatureSetEvent;
import org.cotrix.web.share.shared.feature.Response;
import org.cotrix.web.share.shared.feature.UIFeature;

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
		Set<UIFeature> applicationFeatures = result.getApplicationFeatures()!=null?result.getApplicationFeatures():Collections.<UIFeature>emptySet();
		Map<String, Set<UIFeature>> codelistsFeatures = result.getCodelistsFeatures()!=null?result.getCodelistsFeatures():Collections.<String, Set<UIFeature>>emptyMap();
		featureBus.fireEvent(new NewFeatureSetEvent(applicationFeatures, codelistsFeatures));
		innerCallBack.onSuccess(result.getPayload());
	}

	@Override
	public void onFailure(Throwable caught) {
		innerCallBack.onFailure(caught);
	}


}
