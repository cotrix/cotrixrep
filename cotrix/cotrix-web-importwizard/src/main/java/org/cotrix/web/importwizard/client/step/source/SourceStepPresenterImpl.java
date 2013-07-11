package org.cotrix.web.importwizard.client.step.source;

import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.HasNavigationHandlers;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.NavigationHandler;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.NavigationType;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class SourceStepPresenterImpl implements SourceStepPresenter, HasNavigationHandlers {

	protected HandlerManager handlerManager;
	private final SourceStepView view;
	private final CotrixImportModelController model;

	@Inject
	public SourceStepPresenterImpl(SourceStepView view, CotrixImportModelController model) {
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
		handlerManager = new HandlerManager(this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public WizardStepConfiguration getConfiguration() {
		return new WizardStepConfiguration("Source Selection", "Select source", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.NONE);
	}

	public void go(HasWidgets container) {
		//container.clear();
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return true;
	}

	public void alert(String message) {
		view.alert(message);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public HandlerRegistration addNavigationHandler(NavigationHandler handler) {
		return handlerManager.addHandler(NavigationEvent.TYPE, handler);
	}

	@Override
	public void onCloudButtonClick() {
		Log.trace("onCloudButtonClick");
		//SET the source type in the session
		
		NavigationEvent.fire(handlerManager, NavigationType.FORWARD);
	}

	@Override
	public void onLocalButtonClick() {
		Log.trace("onLocalButtonClick");
		//SET the source type in the session
		NavigationEvent.fire(handlerManager, NavigationType.FORWARD);
	}
}
