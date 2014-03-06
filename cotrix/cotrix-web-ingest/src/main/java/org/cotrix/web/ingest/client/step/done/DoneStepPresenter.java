package org.cotrix.web.ingest.client.step.done;

import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.ImportProgressEvent;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class DoneStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep {
	
	protected static interface DoneStepPresenterEventBinder extends EventBinder<DoneStepPresenter> {}
	
	protected DoneStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public DoneStepPresenter(DoneStepView view, @ImportBus EventBus importEventBus) {
		super("done", TrackerLabels.DONE, "Done", "Done", ImportWizardStepButtons.NEW_IMPORT, ImportWizardStepButtons.MANAGE);
		this.view = view;
		this.importEventBus = importEventBus;
	}
	
	@Inject
	private void bind(DoneStepPresenterEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}

	@EventHandler
	void onImportProgress(ImportProgressEvent event) {
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
