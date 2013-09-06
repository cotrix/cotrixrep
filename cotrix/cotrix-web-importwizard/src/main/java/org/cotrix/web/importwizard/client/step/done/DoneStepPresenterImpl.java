package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class DoneStepPresenterImpl extends AbstractWizardStep implements DoneStepPresenter, ImportProgressHandler {
	
	protected DoneStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public DoneStepPresenterImpl(DoneStepView view, @ImportBus EventBus importEventBus) {
		super("done", TrackerLabels.DONE, "Done", "Done", NavigationButtonConfiguration.NEW_IMPORT, NavigationButtonConfiguration.MANAGE);
		this.view = view;
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ImportProgressEvent.TYPE, this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return true;
	}

	@Override
	public void onImportProgress(ImportProgressEvent event) {
		switch (event.getProgress().getStatus()) {
			case DONE: {
				configuration.setTitle("That's done");
				configuration.setButtons(NavigationButtonConfiguration.NEW_IMPORT, NavigationButtonConfiguration.MANAGE);
				configuration.setSubtitle("Check the log for potential errors or warnings.");
				view.loadReport();
			} break;
			case FAILED: {
				configuration.setTitle("...Oops!");
				configuration.setButtons(NavigationButtonConfiguration.BACKWARD);
				configuration.setSubtitle("Something went wrong, check the log.");
				view.loadReport();
			} break;

			default:
				break;
		}	
		

	}
}
