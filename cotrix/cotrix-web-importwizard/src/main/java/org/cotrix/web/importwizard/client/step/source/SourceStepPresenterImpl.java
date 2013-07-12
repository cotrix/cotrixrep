package org.cotrix.web.importwizard.client.step.source;

import org.cotrix.web.importwizard.client.session.ImportSession;
import org.cotrix.web.importwizard.client.session.SourceType;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.HasNavigationHandlers;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.NavigationHandler;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent.NavigationType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * Source selection step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceStepPresenterImpl implements SourceStepPresenter, HasNavigationHandlers {

	protected HandlerManager handlerManager;
	protected SourceStepView view;
	protected ImportSession session;

	@Inject
	public SourceStepPresenterImpl(SourceStepView view, ImportSession session) {
		this.view = view;
		this.session = session;
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

	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	/** 
	 * {@inheritDoc}
	 */
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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onCloudButtonClick() {
		Log.trace("onCloudButtonClick");
		session.setSourceType(SourceType.CHANNEL);
		NavigationEvent.fire(handlerManager, NavigationType.FORWARD);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onLocalButtonClick() {
		Log.trace("onLocalButtonClick");
		session.setSourceType(SourceType.FILE);
		NavigationEvent.fire(handlerManager, NavigationType.FORWARD);
	}
}
