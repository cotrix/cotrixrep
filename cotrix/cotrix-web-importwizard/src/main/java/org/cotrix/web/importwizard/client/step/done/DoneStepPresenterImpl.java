package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.step.TrackerLabels;
import org.cotrix.web.importwizard.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class DoneStepPresenterImpl extends AbstractVisualWizardStep implements DoneStepPresenter, ImportProgressHandler {
	
	protected DoneStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public DoneStepPresenterImpl(DoneStepView view, @ImportBus EventBus importEventBus) {
		super("done", TrackerLabels.DONE, "Done", "Done", ImportWizardStepButtons.NEW_IMPORT, ImportWizardStepButtons.MANAGE);
		this.view = view;
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ImportProgressEvent.TYPE, this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}

	@Override
	public void onImportProgress(ImportProgressEvent event) {
		switch (event.getProgress().getStatus()) {
			case DONE: {
				configuration.setTitle("That's done");
				configuration.setButtons(ImportWizardStepButtons.NEW_IMPORT, ImportWizardStepButtons.MANAGE);
				configuration.setSubtitle("Check the log for potential errors or warnings.");
				view.loadReport();
			} break;
			case FAILED: {
				configuration.setTitle("...Oops!");
				configuration.setButtons(ImportWizardStepButtons.BACKWARD);
				configuration.setSubtitle("Something went wrong, check the log.");
				view.loadReport();
			} break;

			default:
				break;
		}	
		

	}
}
