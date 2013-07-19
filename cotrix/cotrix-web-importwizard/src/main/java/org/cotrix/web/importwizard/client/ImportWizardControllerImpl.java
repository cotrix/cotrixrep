package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.session.ImportSession;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class ImportWizardControllerImpl implements ImportWizardController {
	
	@Inject
	@ImportBus
	protected EventBus importEventBus;
	
	@Inject
	protected ImportSession session;
	
	@Inject
	protected ImportServiceAsync rpcService;
	
	@Inject
	protected ImportWizardPresenter importWizardPresenter;

	public void go(HasWidgets container) {
		importWizardPresenter.go(container);
	}
}
