package org.cotrix.web.share.client.feature;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.cotrix.web.share.shared.feature.UIFeature;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class NewFeatureSetEvent extends GwtEvent<NewFeatureSetEvent.NewFeatureSetHandler> {

	public static Type<NewFeatureSetHandler> TYPE = new Type<NewFeatureSetHandler>();
	
	private Set<UIFeature> applicationFeatures;
	private Map<String, Set<UIFeature>> codelistsFeatures;

	public interface NewFeatureSetHandler extends EventHandler {
		void onNewFeatureSet(NewFeatureSetEvent event);
	}

	public NewFeatureSetEvent(Set<UIFeature> applicationFeatures, Map<String, Set<UIFeature>> codelistsFeatures) {
		this.applicationFeatures = applicationFeatures;
		this.codelistsFeatures = codelistsFeatures!=null?codelistsFeatures:Collections.<String, Set<UIFeature>>emptyMap();
	}

	public Set<UIFeature> getApplicationFeatures() {
		return applicationFeatures;
	}

	/**
	 * @return the codelistsFeatures
	 */
	public Map<String, Set<UIFeature>> getCodelistsFeatures() {
		return codelistsFeatures;
	}

	@Override
	protected void dispatch(NewFeatureSetHandler handler) {
		handler.onNewFeatureSet(this);
	}

	@Override
	public Type<NewFeatureSetHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NewFeatureSetHandler> getType() {
		return TYPE;
	}
}
