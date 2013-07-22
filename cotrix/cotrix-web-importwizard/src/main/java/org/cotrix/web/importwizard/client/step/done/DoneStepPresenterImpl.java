package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

public class DoneStepPresenterImpl extends AbstractWizardStep implements DoneStepPresenter {
	
	private DoneStepView view;
	
	@Inject
	public DoneStepPresenterImpl(DoneStepView view) {
		super("done","Done", "Done", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.NONE);
		this.view = view;
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return true;
	}
	

}
