/**
 * 
 */
package org.cotrix.web.manage.client.codelists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.cotrix.web.common.client.feature.FeatureBus;
import org.cotrix.web.common.client.feature.event.NewInstancesFeatureSetEvent;
import org.cotrix.web.common.shared.feature.UIFeature;
import org.cotrix.web.manage.client.event.CloseCodelistEvent;
import org.cotrix.web.manage.client.event.ManagerBus;
import org.cotrix.web.manage.client.event.OpenCodelistEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CodelistsFeatureCache {
	
	interface CodelistsFeatureCacheEventBinder extends EventBinder<CodelistsFeatureCache> {}
	
	public interface CodelistsFeatureCacheListener {
		public void onFeatureUpdate(String resourceId, Set<UIFeature> features);
	}
	
	private Map<String, Set<UIFeature>> cache = new HashMap<String, Set<UIFeature>>();
	private List<CodelistsFeatureCacheListener> listeners = new ArrayList<CodelistsFeatureCacheListener>();
	
	@Inject
	private void bind(@FeatureBus EventBus featureBus) {
		featureBus.addHandler(NewInstancesFeatureSetEvent.TYPE, new NewInstancesFeatureSetEvent.NewInstancesFeatureSetHandler() {

			@Override
			public void onNewInstancesFeatureSet(
					NewInstancesFeatureSetEvent event) {
				for (Entry<String, Set<UIFeature>> entry:event.getInstancesFeatures().entrySet()) {
					addNewFeatures(entry.getKey(), entry.getValue());
				}
			}
			
		});
	}
	
	@Inject
	private void bind(CodelistsFeatureCacheEventBinder binder, @ManagerBus EventBus managerBus) {
		binder.bindEventHandlers(this, managerBus);
	}
	
	private void addNewFeatures(String resourceId, Set<UIFeature> features) {
		if (cache.containsKey(resourceId)) {
			cache.put(resourceId, features);
			fireFeatureUpdate(resourceId, features);
		}
	}
	
	@EventHandler
	void onOpenCodelist(OpenCodelistEvent event) {
		String resourceId = event.getCodelist().getId();
		if (!cache.containsKey(resourceId)) cache.put(resourceId, Collections.<UIFeature>emptySet());
	}
	
	@EventHandler
	void onCloseCodelist(CloseCodelistEvent event) {
		cache.remove(event.getCodelist().getId());
	}
	
	public boolean hasFeature(String resourceId, UIFeature feature) {
		return cache.containsKey(resourceId) && cache.get(resourceId).contains(feature);
	}
	
	private void fireFeatureUpdate(String resourceId, Set<UIFeature> features) {
		for (CodelistsFeatureCacheListener listener:listeners) listener.onFeatureUpdate(resourceId, features);
	}

	public void addListener(CodelistsFeatureCacheListener listener) {
		listeners.add(listener);
	}
}
