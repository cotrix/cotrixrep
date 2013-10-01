/**
 * 
 */
package org.cotrix.web.share.client.feature;

import org.cotrix.web.share.client.feature.NewFeatureSetEvent.NewFeatureSetHandler;
import org.cotrix.web.share.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ApplicationFeatureBind implements NewFeatureSetHandler {
	
	protected UIFeature feature;
	protected HasFeature hasFeature;

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
	public void onNewFeatureSet(NewFeatureSetEvent event) {
		if (event.getApplicationFeatures().contains(feature)) hasFeature.setFeature();
		else hasFeature.unsetFeature();
	}
}
