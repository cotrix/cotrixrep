/**
 * 
 */
package org.cotrix.web.share.client.feature;

import org.cotrix.web.share.client.feature.event.NewApplicationFeatureSetEvent;
import org.cotrix.web.share.client.feature.event.NewCodelistsFeatureSetEvent;
import org.cotrix.web.share.client.widgets.HasEditing;
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
	
	public static void bind(HasFeature hasFeature, UIFeature feature)
	{
		ApplicationFeatureBind bind = new ApplicationFeatureBind(feature, hasFeature);
		featureBus.addHandler(NewApplicationFeatureSetEvent.TYPE, bind);
	}
	
	public static void bind(final HasVisibility hasVisibility, UIFeature feature)
	{
		bind(new HasVisibleFeature(hasVisibility), feature);
	}
	
	public static void bind(final HasEnabled hasEnabled, UIFeature feature)
	{
		bind(new HasEnabledFeature(hasEnabled), feature);
	}
	
	public static void bind(HasFeature hasFeature, String codelistId, UIFeature feature)
	{
		CodelistFeatureBind bind = new CodelistFeatureBind(codelistId, feature, hasFeature);
		featureBus.addHandler(NewCodelistsFeatureSetEvent.TYPE, bind);
	}
	
	public static void bind(final HasVisibility hasVisibility, String codelistId, UIFeature feature)
	{
		bind(new HasVisibleFeature(hasVisibility), codelistId, feature);
	}
	
	public static void bind(final HasEnabled hasEnabled, String codelistId, UIFeature feature)
	{
		bind(new HasEnabledFeature(hasEnabled), codelistId, feature);
	}
	
	public static void bind(final HasEditing hasEditing, String codelistId, UIFeature feature)
	{
		bind(new HasEditableFeature(hasEditing), codelistId, feature);
	}

}
