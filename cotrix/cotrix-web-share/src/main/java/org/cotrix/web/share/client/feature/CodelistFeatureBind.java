/**
 * 
 */
package org.cotrix.web.share.client.feature;

import java.util.Set;

import org.cotrix.web.share.client.feature.event.NewCodelistsFeatureSetEvent;
import org.cotrix.web.share.client.feature.event.NewCodelistsFeatureSetEvent.NewCodelistsFeatureSetHandler;
import org.cotrix.web.share.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistFeatureBind implements NewCodelistsFeatureSetHandler {

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
	public void onNewCodelistsFeatureSet(NewCodelistsFeatureSetEvent event) {
		Set<UIFeature> codelistFeatures = event.getCodelistsFeatures().get(codelistId);

		if (codelistFeatures!=null) {
			if (codelistFeatures.contains(feature)) hasFeature.setFeature();
			else hasFeature.unsetFeature();
		}
	}

}
