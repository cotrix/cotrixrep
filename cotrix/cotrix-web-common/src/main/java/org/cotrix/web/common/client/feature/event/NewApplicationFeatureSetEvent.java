package org.cotrix.web.common.client.feature.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import java.util.Set;

import org.cotrix.web.common.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NewApplicationFeatureSetEvent extends GwtEvent<NewApplicationFeatureSetEvent.NewApplicationFeatureSetHandler> {

	public static Type<NewApplicationFeatureSetHandler> TYPE = new Type<NewApplicationFeatureSetHandler>();
	
	private Set<UIFeature> features;

	public interface NewApplicationFeatureSetHandler extends EventHandler {
		void onNewApplicationFeatureSet(NewApplicationFeatureSetEvent event);
	}

	public NewApplicationFeatureSetEvent(Set<UIFeature> features) {
		this.features = features;
	}

	public Set<UIFeature> getFeatures() {
		return features;
	}

	@Override
	protected void dispatch(NewApplicationFeatureSetHandler handler) {
		handler.onNewApplicationFeatureSet(this);
	}

	@Override
	public Type<NewApplicationFeatureSetHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NewApplicationFeatureSetHandler> getType() {
		return TYPE;
	}
}
