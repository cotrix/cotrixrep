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
public abstract class AbstractApplicationFeatureBind implements NewFeatureSetHandler {
	
	protected UIFeature feature;

	/**
	 * @param feature
	 */
	public AbstractApplicationFeatureBind(UIFeature feature) {
		this.feature = feature;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onNewFeatureSet(NewFeatureSetEvent event) {
		if (event.getApplicationFeatures().contains(feature)) setFeature();
		else unsetFeature();
	}
	
	public abstract void setFeature();
	public abstract void unsetFeature();
	
	

}
