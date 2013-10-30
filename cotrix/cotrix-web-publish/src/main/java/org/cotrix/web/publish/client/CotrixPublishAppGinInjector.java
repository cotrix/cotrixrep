package org.cotrix.web.publish.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules(CotrixPublishAppGinModule.class)
public interface CotrixPublishAppGinInjector extends Ginjector {
	public PublishServiceAsync getRpcService();
	public CotrixPublishAppController getAppController();

}
