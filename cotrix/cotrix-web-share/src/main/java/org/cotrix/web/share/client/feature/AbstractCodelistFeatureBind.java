/**
 * 
 */
package org.cotrix.web.share.client.feature;

import java.util.Set;

import org.cotrix.web.share.client.feature.NewFeatureSetEvent.NewFeatureSetHandler;
import org.cotrix.web.share.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractCodelistFeatureBind implements NewFeatureSetHandler {

	protected String codelistId;
	protected UIFeature feature;

	/**
	 * @param feature
	 */
	public AbstractCodelistFeatureBind(String codelistId, UIFeature feature) {
		this.codelistId = codelistId;
		this.feature = feature;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onNewFeatureSet(NewFeatureSetEvent event) {
		Set<UIFeature> codelistFeatures = event.getCodelistsFeatures().get(codelistId);

		if (codelistFeatures!=null) {
			if (codelistFeatures.contains(feature)) setFeature();
			else unsetFeature();
		}
	}

	public abstract void setFeature();
	public abstract void unsetFeature();



}
