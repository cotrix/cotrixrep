/**
 * 
 */
package org.cotrix.web.share.client.feature;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.share.client.rpc.CallBackListener;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.UIFeature;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FeatureInterceptor implements CallBackListener {
	
	@Inject @FeatureBus
	protected static EventBus featureBus;

	@Override
	public boolean onFailure(Throwable caught) {
		return true;
	}

	@Override
	public boolean onSuccess(Object result) {
		if (result instanceof FeatureCarrier) {
			FeatureCarrier response = (FeatureCarrier) result;
			Set<UIFeature> applicationFeatures = response.getApplicationFeatures()!=null?response.getApplicationFeatures():Collections.<UIFeature>emptySet();
			Map<String, Set<UIFeature>> codelistsFeatures = response.getCodelistsFeatures()!=null?response.getCodelistsFeatures():Collections.<String, Set<UIFeature>>emptyMap();
			featureBus.fireEvent(new NewFeatureSetEvent(applicationFeatures, codelistsFeatures));
		}
		return true;
	}

}
