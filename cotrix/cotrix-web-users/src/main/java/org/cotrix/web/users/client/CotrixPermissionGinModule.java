package org.cotrix.web.users.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixPermissionGinModule extends AbstractGinModule {

	protected void configure() {
		bind(EventBus.class).annotatedWith(PermissionBus.class).to(SimpleEventBus.class).in(Singleton.class);
	}
}
