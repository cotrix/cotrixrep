package org.cotrix.web.publish.client;

import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.resources.Resources;
import org.cotrix.web.publish.client.wizard.PublishWizardPresenter;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CotrixPublishController implements CotrixModuleController {

	@Inject
	protected PublishWizardPresenter presenter;
	
	@Inject
	@PublishBus
	protected EventBus publishBus;
	
	@Inject
	public CotrixPublishController() {
	}
	
	@Inject
	private void initResources() {
		Resources.INSTANCE.css().ensureInjected();
		Resources.INSTANCE.definitionsMapping().ensureInjected();
	}

	@Override
	public CotrixModule getModule() {
		return CotrixModule.PUBLISH;
	}

	@Override
	public void go(HasWidgets container) {
		presenter.go(container);
	}

	@Override
	public void activate() {
		publishBus.fireEvent(new ResetWizardEvent());
	}

	@Override
	public void deactivate() {
	}

}
