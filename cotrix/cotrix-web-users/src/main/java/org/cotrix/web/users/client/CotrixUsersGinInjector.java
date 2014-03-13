package org.cotrix.web.users.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import org.cotrix.web.common.client.CommonGinModule;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@GinModules({CotrixUsersGinModule.class, CommonGinModule.class})
public interface CotrixUsersGinInjector extends Ginjector {
	
	public static CotrixUsersGinInjector INSTANCE = GWT.create(CotrixUsersGinInjector.class);
	
	public CotrixUsersController getController();
	
	public UsersPanel getPermissionManagerPanel();

}
