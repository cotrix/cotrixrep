/**
 * 
 */
package org.cotrix.web.share.client.feature;

import java.util.Map;
import java.util.Set;

import org.cotrix.web.share.client.feature.event.NewApplicationFeatureSetEvent;
import org.cotrix.web.share.client.feature.event.NewCodelistsFeatureSetEvent;
import org.cotrix.web.share.client.rpc.CallBackListener;
import org.cotrix.web.share.shared.feature.FeatureCarrier;
import org.cotrix.web.share.shared.feature.UIFeature;

import com.allen_sauer.gwt.log.client.Log;
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
			Log.trace("intercepted FeatureCarrier "+result.getClass().getName()+" applicationFeatures: "+response.getApplicationFeatures()+" codelistsFeatures: "+response.getCodelistsFeatures());

			if (response.getApplicationFeatures()!=null) {
				Set<UIFeature> applicationFeatures = response.getApplicationFeatures();
				Log.trace("broadcasting application features "+applicationFeatures);
				featureBus.fireEvent(new NewApplicationFeatureSetEvent(applicationFeatures));
			}
			
			if (response.getCodelistsFeatures()!=null) {
				Map<String, Set<UIFeature>> codelistsFeatures = response.getCodelistsFeatures();
				Log.trace("broadcasting codelists features "+codelistsFeatures);
				featureBus.fireEvent(new NewCodelistsFeatureSetEvent(codelistsFeatures));
			}
		}
		return true;
	}

}
