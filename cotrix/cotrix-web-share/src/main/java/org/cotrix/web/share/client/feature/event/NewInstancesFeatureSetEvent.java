package org.cotrix.web.share.client.feature.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import java.util.Map;
import java.util.Set;

import org.cotrix.web.share.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NewInstancesFeatureSetEvent extends GwtEvent<NewInstancesFeatureSetEvent.NewInstancesFeatureSetHandler> {

	public static Type<NewInstancesFeatureSetHandler> TYPE = new Type<NewInstancesFeatureSetHandler>();
	
	private Map<String, Set<UIFeature>> instancesFeatures;

	public interface NewInstancesFeatureSetHandler extends EventHandler {
		void onNewInstancesFeatureSet(NewInstancesFeatureSetEvent event);
	}

	public NewInstancesFeatureSetEvent(Map<String, Set<UIFeature>> instancesFeatures) {
		this.instancesFeatures = instancesFeatures;
	}

	/**
	 * @return the instancesFeatures
	 */
	public Map<String, Set<UIFeature>> getInstancesFeatures() {
		return instancesFeatures;
	}

	@Override
	protected void dispatch(NewInstancesFeatureSetHandler handler) {
		handler.onNewInstancesFeatureSet(this);
	}

	@Override
	public Type<NewInstancesFeatureSetHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NewInstancesFeatureSetHandler> getType() {
		return TYPE;
	}
}
