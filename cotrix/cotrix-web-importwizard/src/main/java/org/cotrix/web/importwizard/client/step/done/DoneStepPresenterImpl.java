package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class DoneStepPresenterImpl implements DoneStepPresenter {
	
	private ImportServiceAsync rpcService;
	private HandlerManager eventBus;
	private DoneStepView view;
	private CotrixImportModelController model;
	
	@Inject
	public DoneStepPresenterImpl(ImportServiceAsync rpcService, HandlerManager eventBus, DoneStepView view,CotrixImportModelController model) {
		this.rpcService = rpcService;
		this.eventBus = eventBus;
		this.view = view;
		this.view.setPresenter(this);
		this.model = model;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public WizardStepConfiguration getConfiguration() {
		return new WizardStepConfiguration("Done", "Done", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.NONE);
	}

	
	public void go(HasWidgets container) {
		//container.clear();
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return true;
	}
	
	public void setDoneTitle(String title){
		view.setDoneTitle(title);
	}
	public void setWarningMessage(String message){
		view.setWarningMessage(message);
	}

	
}
