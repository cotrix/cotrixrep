package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.resources.Resources;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration.ButtonAction;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class DoneStepPresenterImpl extends AbstractWizardStep implements DoneStepPresenter, ImportProgressHandler {
	
	protected static final NavigationButtonConfiguration NEW_IMPORT = new NavigationButtonConfiguration("Import another", ButtonAction.NEW_IMPORT, Resources.INSTANCE.css().blueButton());
	protected static final NavigationButtonConfiguration MANAGE = new NavigationButtonConfiguration("Manage it", ButtonAction.MANAGE, Resources.INSTANCE.css().blueButton());
	
	protected DoneStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public DoneStepPresenterImpl(DoneStepView view, @ImportBus EventBus importEventBus) {
		super("done", TrackerLabels.DONE, "Done", "Done", NEW_IMPORT, MANAGE);
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
				configuration.setBackwardButton(NEW_IMPORT);
				configuration.setForwardButton(MANAGE);
				configuration.setSubtitle("Here's a log of what's happened.");
				view.loadReport();
			} break;
			case FAILED: {
				configuration.setTitle("...Oops!");
				configuration.setBackwardButton(NavigationButtonConfiguration.DEFAULT_BACKWARD);
				configuration.setForwardButton(NavigationButtonConfiguration.NONE);
				configuration.setSubtitle("Something went wrong, check the log.");
				view.loadReport();
			} break;

			default:
				break;
		}	
		

	}
}
