package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.NewImportEvent;
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
		super("done","Done", "Done", NavigationButtonConfiguration.NONE, NavigationButtonConfiguration.NONE);
		this.view = view;
		view.setPresenter(this);
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
				view.setStepTitle("Codelist successfully imported");
				view.setMessage(event.getProgress().getReport());
			} break;
			case FAILED: {
				view.setStepTitle("Codelist import failed");
				view.setMessage(event.getProgress().getReport());
			} break;

			default:
				break;
		}		
	}

	@Override
	public void importButtonClicked() {
		importEventBus.fireEvent(new NewImportEvent());
	}

	@Override
	public void manageButtonClicked() {
		// TODO Auto-generated method stub
		
	}
}
