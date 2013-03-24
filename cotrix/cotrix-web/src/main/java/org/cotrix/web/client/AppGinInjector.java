package org.cotrix.web.client;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(AppGinModule.class)
public interface AppGinInjector extends Ginjector {
	public AppController getAppController();
}
