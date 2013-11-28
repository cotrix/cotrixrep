package org.cotrix.web.permissionmanager.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import org.cotrix.web.share.client.CommonGinModule;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixPermissionGinModule.class, CommonGinModule.class})
public interface CotrixPermissionGinInjector extends Ginjector {
	
	public static CotrixPermissionGinInjector INSTANCE = GWT.create(CotrixPermissionGinInjector.class);
	
	public CotrixPermissionManagerController getController();
	
	public PermissionManagerPanel getPermissionManagerPanel();

}
