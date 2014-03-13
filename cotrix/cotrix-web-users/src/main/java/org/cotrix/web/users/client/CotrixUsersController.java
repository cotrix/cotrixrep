package org.cotrix.web.users.client;

import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CotrixUsersController implements CotrixModuleController {

	@Inject
	protected UsersPanel permissionManagerPanel;
	
	@Inject @UsersBus
	protected EventBus permissionBus;
	
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
		permissionBus.fireEvent(new ModuleActivactedEvent());
	}

	@Override
	public void deactivate() {
		
	}
	
}
