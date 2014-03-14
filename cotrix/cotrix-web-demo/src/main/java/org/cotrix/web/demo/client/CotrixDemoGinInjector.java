package org.cotrix.web.demo.client;

import org.cotrix.web.common.client.CommonGinModule;
import org.cotrix.web.demo.client.credential.CredentialsPopupController;
import org.cotrix.web.demo.client.resources.CotrixDemoResources;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixDemoGinModule.class, CommonGinModule.class})
public interface CotrixDemoGinInjector extends Ginjector {
	public CredentialsPopupController getCredentialPopupController();
	public CotrixDemoResources getResources();
}
