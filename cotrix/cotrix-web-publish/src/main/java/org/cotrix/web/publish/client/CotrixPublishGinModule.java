package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.event.PublishBus;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixPublishGinModule extends AbstractGinModule {
    
	@Override
	protected void configure() {
	   	bind(EventBus.class).annotatedWith(PublishBus.class).to(SimpleEventBus.class).in(Singleton.class);
	}

}
