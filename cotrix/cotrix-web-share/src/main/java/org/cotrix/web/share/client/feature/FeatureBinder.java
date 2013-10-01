/**
 * 
 */
package org.cotrix.web.share.client.feature;

import org.cotrix.web.share.shared.feature.UIFeature;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class FeatureBinder {
	
	@Inject @FeatureBus
	protected static EventBus featureBus;
	
	public static void bind(final HasVisibility hasVisibility, UIFeature feature)
	{
		AbstractApplicationFeatureBind bind = new AbstractApplicationFeatureBind(feature) {
			
			@Override
			public void unsetFeature() {
				hasVisibility.setVisible(false);				
			}
			
			@Override
			public void setFeature() {
				hasVisibility.setVisible(true);
			}
		};
		featureBus.addHandler(NewFeatureSetEvent.TYPE, bind);
	}
	
	public static void bind(final HasEnabled hasEnabled, UIFeature feature)
	{
		AbstractApplicationFeatureBind bind = new AbstractApplicationFeatureBind(feature) {
			
			@Override
			public void unsetFeature() {
				hasEnabled.setEnabled(false);
			}
			
			@Override
			public void setFeature() {
				hasEnabled.setEnabled(true);
			}
		};
		featureBus.addHandler(NewFeatureSetEvent.TYPE, bind);
	}
	
	public static void bind(final HasVisibility hasVisibility, String codelistId, UIFeature feature)
	{
		AbstractCodelistFeatureBind bind = new AbstractCodelistFeatureBind(codelistId, feature) {
			
			@Override
			public void unsetFeature() {
				hasVisibility.setVisible(false);				
			}
			
			@Override
			public void setFeature() {
				hasVisibility.setVisible(true);
			}
		};
		featureBus.addHandler(NewFeatureSetEvent.TYPE, bind);
	}
	
	public static void bind(final HasEnabled hasEnabled, String codelistId, UIFeature feature)
	{
		AbstractCodelistFeatureBind bind = new AbstractCodelistFeatureBind(codelistId, feature) {
			
			@Override
			public void unsetFeature() {
				hasEnabled.setEnabled(false);
			}
			
			@Override
			public void setFeature() {
				hasEnabled.setEnabled(true);
			}
		};
		featureBus.addHandler(NewFeatureSetEvent.TYPE, bind);
	}

}
