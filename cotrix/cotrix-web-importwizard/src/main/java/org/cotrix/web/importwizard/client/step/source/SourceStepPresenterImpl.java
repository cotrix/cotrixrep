package org.cotrix.web.importwizard.client.step.source;

import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.share.shared.CotrixImportModelController;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class SourceStepPresenterImpl implements SourceStepPresenter {

	private final SourceStepView view;
	private final CotrixImportModelController model;

	@Inject
	public SourceStepPresenterImpl(SourceStepView view,CotrixImportModelController model) {
		this.view = view;
		this.model = model;
		this.view.setPresenter(this);
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
		if(view.isValidated()){
			model.setMetadata(view.getMetadata());
		}
		return view.isValidated();
	}

	public void alert(String message) {
		view.alert(message);
	}


	
}
