package org.cotrix.web.permissionmanager.client;

import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.CotrixModuleController;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CotrixPermissionManagerController implements CotrixModuleController {

	@Inject
	protected PermissionManagerPanel permissionManagerPanel;
	
	@Override
	public CotrixModule getModule() {
		return CotrixModule.PERMISSION;
	}

	@Override
	public void go(HasWidgets container) {
		container.add(permissionManagerPanel);		
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void deactivate() {
		
	}
	
}
