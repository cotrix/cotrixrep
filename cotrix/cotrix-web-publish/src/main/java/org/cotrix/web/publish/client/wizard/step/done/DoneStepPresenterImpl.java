package org.cotrix.web.publish.client.wizard.step.done;

import org.cotrix.web.publish.client.event.PublishProgressEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.event.PublishProgressEvent.PublishProgressHandler;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DoneStepPresenterImpl extends AbstractVisualWizardStep implements DoneStepPresenter, PublishProgressHandler {
	
	protected DoneStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public DoneStepPresenterImpl(DoneStepView view, @PublishBus EventBus importEventBus) {
		super("done", TrackerLabels.DONE, "Done", "Done", PublishWizardStepButtons.NEW_PUBLISH, PublishWizardStepButtons.MANAGE);
		this.view = view;
		this.importEventBus = importEventBus;
		importEventBus.addHandler(PublishProgressEvent.TYPE, this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}

	@Override
	public void onPublishProgress(PublishProgressEvent event) {
		switch (event.getProgress().getStatus()) {
			case DONE: {
				configuration.setTitle("That's done");
				configuration.setButtons(PublishWizardStepButtons.NEW_PUBLISH, PublishWizardStepButtons.MANAGE);
				configuration.setSubtitle("Check the log for potential errors or warnings.");
				view.loadReport();
			} break;
			case FAILED: {
				configuration.setTitle("...Oops!");
				configuration.setButtons(PublishWizardStepButtons.BACKWARD);
				configuration.setSubtitle("Something went wrong, check the log.");
				view.loadReport();
			} break;

			default:
				break;
		}	
		

	}
}
