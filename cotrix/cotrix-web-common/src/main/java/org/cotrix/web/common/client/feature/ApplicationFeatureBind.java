/**
 * 
 */
package org.cotrix.web.common.client.feature;

import java.util.Set;

import org.cotrix.web.common.client.feature.event.NewApplicationFeatureSetEvent;
import org.cotrix.web.common.client.feature.event.NewApplicationFeatureSetEvent.NewApplicationFeatureSetHandler;
import org.cotrix.web.common.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ApplicationFeatureBind implements NewApplicationFeatureSetHandler {
	
	private UIFeature feature;
	private HasFeature hasFeature;

	/**
	 * @param feature
	 */
	public ApplicationFeatureBind(UIFeature feature, HasFeature hasFeature) {
		this.feature = feature;
		this.hasFeature = hasFeature;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onNewApplicationFeatureSet(NewApplicationFeatureSetEvent event) {
		newFeatures(event.getFeatures());
	}
	
	public void newFeatures(Set<UIFeature> features) {
		if (features.contains(feature)) hasFeature.setFeature();
		else hasFeature.unsetFeature();
	}
}
