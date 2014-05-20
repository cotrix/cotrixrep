package org.cotrix.web.client;

import org.cotrix.web.client.userbar.UserController;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixGinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(UserController.class).asEagerSingleton();
	}

}
