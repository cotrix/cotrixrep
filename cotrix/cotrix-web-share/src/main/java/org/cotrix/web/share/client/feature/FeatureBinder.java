/**
 * 
 */
package org.cotrix.web.share.client.feature;

import org.cotrix.web.share.client.feature.InstanceFeatureBind.IdProvider;
import org.cotrix.web.share.client.feature.InstanceFeatureBind.StaticId;
import org.cotrix.web.share.client.feature.event.NewApplicationFeatureSetEvent;
import org.cotrix.web.share.client.feature.event.NewInstancesFeatureSetEvent;
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
	
	public static void bind(HasFeature hasFeature, String instanceId, UIFeature feature)
	{
		InstanceFeatureBind bind = new InstanceFeatureBind(new StaticId(instanceId), feature, hasFeature);
		featureBus.addHandler(NewInstancesFeatureSetEvent.TYPE, bind);
	}
	
	public static void bind(final HasVisibility hasVisibility, String instanceId, UIFeature feature)
	{
		bind(new HasVisibleFeature(hasVisibility), instanceId, feature);
	}
	
	public static void bind(final HasEnabled hasEnabled, String instanceId, UIFeature feature)
	{
		bind(new HasEnabledFeature(hasEnabled), instanceId, feature);
	}
	
	public static void bind(final HasEditing hasEditing, String instanceId, UIFeature feature)
	{
		bind(new HasEditableFeature(hasEditing), instanceId, feature);
	}
	
	public static void bind(HasFeature hasFeature, IdProvider idProvider, UIFeature feature)
	{
		InstanceFeatureBind bind = new InstanceFeatureBind(idProvider, feature, hasFeature);
		featureBus.addHandler(NewInstancesFeatureSetEvent.TYPE, bind);
	}

}
