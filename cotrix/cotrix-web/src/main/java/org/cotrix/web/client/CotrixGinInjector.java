package org.cotrix.web.client;

import org.cotrix.web.client.resources.CotrixResources;
import org.cotrix.web.common.client.CommonGinModule;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixGinModule.class, CommonGinModule.class})
public interface CotrixGinInjector extends Ginjector {

	public CotrixController getAppController();
	
	public CotrixResources getCotrixResources();
}
