/**
 * 
 */
package org.cotrix.web.common.client.feature;

import java.util.Set;

import org.cotrix.web.common.client.feature.event.NewInstancesFeatureSetEvent;
import org.cotrix.web.common.client.feature.event.NewInstancesFeatureSetEvent.NewInstancesFeatureSetHandler;
import org.cotrix.web.common.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class InstanceFeatureBind implements NewInstancesFeatureSetHandler {
	
	public static interface IdProvider {
		String getId();
	}
	
	public static class StaticId implements IdProvider {
		
		protected String id;

		public StaticId(String id) {
			this.id = id;
		}
		@Override
		public String getId() {
			return id;
		}
		
	}

	protected IdProvider idProvider;
	protected UIFeature feature;
	protected HasFeature hasFeature;

	/**
	 * @param feature
	 */
	public InstanceFeatureBind(IdProvider idProvider, UIFeature feature, HasFeature hasFeature) {
		this.idProvider = idProvider;
		this.feature = feature;
		this.hasFeature = hasFeature;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onNewInstancesFeatureSet(NewInstancesFeatureSetEvent event) {
		String instanceId = idProvider.getId();
		if (instanceId == null) return;
		
		Set<UIFeature> instanceFeatures = event.getInstancesFeatures().get(instanceId);

		if (instanceFeatures!=null) {
			if (instanceFeatures.contains(feature)) hasFeature.setFeature();
			else hasFeature.unsetFeature();
		}
	}

}
