package org.cotrix.web.client;

import org.cotrix.web.share.client.CommonGinModule;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({AppGinModule.class, CommonGinModule.class})
public interface AppGinInjector extends Ginjector {
	public AppController getAppController();
}
