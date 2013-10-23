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
public class NewCodelistsFeatureSetEvent extends GwtEvent<NewCodelistsFeatureSetEvent.NewCodelistsFeatureSetHandler> {

	public static Type<NewCodelistsFeatureSetHandler> TYPE = new Type<NewCodelistsFeatureSetHandler>();
	
	private Map<String, Set<UIFeature>> codelistsFeatures;

	public interface NewCodelistsFeatureSetHandler extends EventHandler {
		void onNewCodelistsFeatureSet(NewCodelistsFeatureSetEvent event);
	}

	public NewCodelistsFeatureSetEvent(Map<String, Set<UIFeature>> codelistsFeatures) {
		this.codelistsFeatures = codelistsFeatures;
	}

	/**
	 * @return the codelistsFeatures
	 */
	public Map<String, Set<UIFeature>> getCodelistsFeatures() {
		return codelistsFeatures;
	}

	@Override
	protected void dispatch(NewCodelistsFeatureSetHandler handler) {
		handler.onNewCodelistsFeatureSet(this);
	}

	@Override
	public Type<NewCodelistsFeatureSetHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NewCodelistsFeatureSetHandler> getType() {
		return TYPE;
	}
}
