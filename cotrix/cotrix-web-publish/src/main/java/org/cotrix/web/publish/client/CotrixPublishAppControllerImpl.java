package org.cotrix.web.publish.client;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.resources.Resources;
import org.cotrix.web.publish.client.wizard.PublishWizardPresenter;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CotrixPublishAppControllerImpl implements CotrixPublishAppController {

	@Inject
	protected PublishWizardPresenter presenter;
	
	@Inject
	protected PublishController controller;
	
	@Inject
	@PublishBus
	protected EventBus publishBus;
	
	@Inject
	public CotrixPublishAppControllerImpl() {
	}

	@Override
	public CotrixModule getModule() {
		return CotrixModule.PUBLISH;
	}

	@Override
	public void go(HasWidgets container) {
		Resources.INSTANCE.css().ensureInjected();
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
