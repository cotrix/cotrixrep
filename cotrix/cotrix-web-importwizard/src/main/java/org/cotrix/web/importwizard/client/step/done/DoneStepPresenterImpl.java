package org.cotrix.web.importwizard.client.step.done;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.event.ImportStartedEvent;
import org.cotrix.web.importwizard.client.event.ImportStartedEvent.ImportStartedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class DoneStepPresenterImpl extends AbstractWizardStep implements DoneStepPresenter, ImportStartedHandler, ImportProgressHandler {
	
	private DoneStepView view;
	
	@Inject
	public DoneStepPresenterImpl(DoneStepView view, @ImportBus EventBus importEventBus) {
		super("done","Done", "Done", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.NONE);
		this.view = view;
		importEventBus.addHandler(ImportStartedEvent.TYPE, this);
		importEventBus.addHandler(ImportProgressEvent.TYPE, this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return true;
	}
	
	protected void mask()
	{
		view.mask();
	}
	
	protected void unmask()
	{
		view.unmask();
	}

	@Override
	public void onImportProgress(ImportProgressEvent event) {
		switch (event.getProgress().getStatus()) {
			case DONE: {
				view.setTitle("COMPLETE");
				view.setMessage(event.getProgress().getReport());
			} break;
			case FAILED: {
				view.setTitle("FAILED");
				view.setMessage(event.getProgress().getReport());
			} break;

			default:
				break;
		}		
	}

	@Override
	public void onImportStarted(ImportStartedEvent event) {
		mask();		
	}
	

}
