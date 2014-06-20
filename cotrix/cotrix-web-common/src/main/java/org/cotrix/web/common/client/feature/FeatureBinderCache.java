/**
 * 
 */
package org.cotrix.web.common.client.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.common.client.feature.event.NewApplicationFeatureSetEvent;
import org.cotrix.web.common.client.feature.event.NewInstancesFeatureSetEvent;
import org.cotrix.web.common.shared.feature.UIFeature;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class FeatureBinderCache {
	
	@Inject @FeatureBus
	private EventBus featureBus;
	
	private boolean cacheActive;
	private Map<String, Set<UIFeature>> instancesCache = new HashMap<String, Set<UIFeature>>();
	private HandlerRegistration instancesRegistration;
	private Set<UIFeature> applicationFeaturesCache;
	private HandlerRegistration applicationRegistration;
	
	public void turnOn() {
		if (cacheActive) return;
		
		applicationRegistration = featureBus.addHandler(NewApplicationFeatureSetEvent.TYPE,new NewApplicationFeatureSetEvent.NewApplicationFeatureSetHandler() {
			
			@Override
			public void onNewApplicationFeatureSet(NewApplicationFeatureSetEvent event) {
				applicationFeaturesCache = event.getFeatures();
			}
		});
		
		instancesRegistration = featureBus.addHandler(NewInstancesFeatureSetEvent.TYPE, new NewInstancesFeatureSetEvent.NewInstancesFeatureSetHandler() {
			
			@Override
			public void onNewInstancesFeatureSet(NewInstancesFeatureSetEvent event) {
				instancesCache.putAll(event.getInstancesFeatures());
			}
		});
	}
	
	public void initializeBind(InstanceFeatureBind bind) {
		if (!cacheActive) return;
		bind.newFeatures(instancesCache);
	}
	
	public void initializeBind(ApplicationFeatureBind bind) {
		if (!cacheActive) return;
		bind.newFeatures(applicationFeaturesCache);
	}
	
	public void turnOff() {
		if (!cacheActive) return;
		cacheActive = false;
		instancesRegistration.removeHandler();
		instancesRegistration = null;
		applicationRegistration.removeHandler();
		applicationRegistration = null;
		instancesCache = null;
		applicationFeaturesCache = null;
	}

}
