package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class DoneStepPresenterImpl extends AbstractWizardStep implements DoneStepPresenter, ImportProgressHandler {
	
	private DoneStepView view;
	
	@Inject
	public DoneStepPresenterImpl(DoneStepView view, @ImportBus EventBus importEventBus) {
		super("done","Done", "Done", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.NONE);
		this.view = view;
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
				view.setTitle("Codelist successfully imported");
				view.setMessage(event.getProgress().getReport());
			} break;
			case FAILED: {
				view.setTitle("Codelist import failed");
				view.setMessage(event.getProgress().getReport());
			} break;

			default:
				break;
		}		
	}
}
