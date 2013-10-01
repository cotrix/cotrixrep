package org.cotrix.web.share.client;

import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.FeatureAsyncCallBack;
import org.cotrix.web.share.client.feature.FeatureBinder;
import org.cotrix.web.share.client.feature.FeatureBus;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CommonGinModule extends AbstractGinModule {
	
	/**
	 * Common instance for all modules
	 */
	protected static EventBus cotrixBus = new SimpleEventBus();
	
	protected static EventBus featureBus = new SimpleEventBus();
	
	@Provides
	@Singleton
	@CotrixBus
	protected EventBus getCotrixBus()
	{
		return cotrixBus;
	}
	
	@Provides
	@Singleton
	@FeatureBus
	protected EventBus getFeatureBus()
	{
		return featureBus;
	}

	@Override
	protected void configure() {
		
		requestStaticInjection(FeatureBinder.class);
		requestStaticInjection(FeatureAsyncCallBack.class);
	}

}
