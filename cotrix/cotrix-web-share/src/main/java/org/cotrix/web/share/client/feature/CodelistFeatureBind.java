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
public class CodelistFeatureBind implements NewFeatureSetHandler {

	protected String codelistId;
	protected UIFeature feature;
	protected HasFeature hasFeature;

	/**
	 * @param feature
	 */
	public CodelistFeatureBind(String codelistId, UIFeature feature, HasFeature hasFeature) {
		this.codelistId = codelistId;
		this.feature = feature;
		this.hasFeature = hasFeature;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onNewFeatureSet(NewFeatureSetEvent event) {
		Set<UIFeature> codelistFeatures = event.getCodelistsFeatures().get(codelistId);

		if (codelistFeatures!=null) {
			if (codelistFeatures.contains(feature)) hasFeature.setFeature();
			else hasFeature.unsetFeature();
		}
	}

}
