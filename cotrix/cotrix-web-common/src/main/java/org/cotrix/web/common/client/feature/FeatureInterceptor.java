/**
 * 
 */
package org.cotrix.web.common.client.feature;

import java.util.Map;
import java.util.Set;

import org.cotrix.web.common.client.feature.event.NewApplicationFeatureSetEvent;
import org.cotrix.web.common.client.feature.event.NewInstancesFeatureSetEvent;
import org.cotrix.web.common.client.rpc.CallBackListener;
import org.cotrix.web.common.shared.feature.AbstractFeatureCarrier;
import org.cotrix.web.common.shared.feature.FeatureCarrier;
import org.cotrix.web.common.shared.feature.UIFeature;

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
		if (result instanceof AbstractFeatureCarrier) {
			FeatureCarrier response = (FeatureCarrier) result;

			if (response.getApplicationFeatures()!=null) {
				Set<UIFeature> applicationFeatures = response.getApplicationFeatures();
				Log.trace("broadcasting application features "+applicationFeatures);
				featureBus.fireEvent(new NewApplicationFeatureSetEvent(applicationFeatures));
			}
			
			if (response.getInstancesFeatures()!=null) {
				Map<String, Set<UIFeature>> instancesFeatures = response.getInstancesFeatures();
				Log.trace("broadcasting instances features "+instancesFeatures);
				featureBus.fireEvent(new NewInstancesFeatureSetEvent(instancesFeatures));
			}
		}
		return true;
	}

}
