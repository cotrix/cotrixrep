package org.cotrix.web.common.client;

import org.cotrix.web.common.client.error.CallbackFailureLogger;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.error.ManagedFailureLongCallback;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.feature.FeatureBus;
import org.cotrix.web.common.client.feature.FeatureInterceptor;
import org.cotrix.web.common.client.feature.UserProvider;
import org.cotrix.web.common.client.rpc.CallBackListenerManager;
import org.cotrix.web.common.client.rpc.CotrixRemoteServiceProxy;
import org.cotrix.web.common.client.util.StatusUpdates;

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
	private static EventBus cotrixBus = new SimpleEventBus();
	
	private static EventBus featureBus = new SimpleEventBus();
	
	private static CallBackListenerManager callbackListenerManager = new CallBackListenerManager();
	
	static {
		callbackListenerManager.registerInterceptor(new FeatureInterceptor());
		callbackListenerManager.registerInterceptor(new CallbackFailureLogger());
	}
	
	private static UserProvider userProvider = new UserProvider();
	
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
	
	@Provides
	@Singleton
	protected CallBackListenerManager getCallBackListenerManager()
	{
		return callbackListenerManager;
	}
	
	@Provides
	@Singleton
	protected UserProvider getUserProvider()
	{
		return userProvider;
	}

	@Override
	protected void configure() {
		
		requestStaticInjection(FeatureInterceptor.class);
		requestStaticInjection(ManagedFailureCallback.class);
		requestStaticInjection(ManagedFailureLongCallback.class);
		requestStaticInjection(CotrixRemoteServiceProxy.class);
		requestStaticInjection(StatusUpdates.class);
	}

}
