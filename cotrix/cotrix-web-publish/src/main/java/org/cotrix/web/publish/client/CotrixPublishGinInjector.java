package org.cotrix.web.publish.client;

import org.cotrix.web.common.client.CommonGinModule;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixPublishGinModule.class, CommonGinModule.class})
public interface CotrixPublishGinInjector extends Ginjector {
	
	public static final CotrixPublishGinInjector INSTANCE = GWT.create(CotrixPublishGinInjector.class);
	
	public PublishServiceAsync getRpcService();
	public CotrixPublishController getController();

}
