package org.cotrix.web.publish.client;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixPublishAppGinModule extends AbstractGinModule {
    
	@Override
	protected void configure() {
		bind(CotrixPublishAppController.class).to(CotrixPublishAppControllerImpl.class);

	}

}
