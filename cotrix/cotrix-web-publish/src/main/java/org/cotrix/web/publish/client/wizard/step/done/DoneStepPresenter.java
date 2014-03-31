package org.cotrix.web.publish.client.wizard.step.done;

import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.Progress.Status;
import org.cotrix.web.publish.client.event.PublishCompleteEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class DoneStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep {
	
	protected DoneStepView view;
	protected EventBus publishBus;
	
	@Inject
	public DoneStepPresenter(DoneStepView view, @PublishBus EventBus publishBus) {
		super("done", TrackerLabels.DONE, "Done", "Done", PublishWizardStepButtons.NEW_PUBLISH);
		this.view = view;
		this.publishBus = publishBus;
		bind();
	}
	
	protected void bind() {
		publishBus.addHandler(PublishCompleteEvent.TYPE, new PublishCompleteEvent.PublishCompleteHandler() {
			
			@Override
			public void onPublishComplete(PublishCompleteEvent event) {
				
				Progress progress = event.getProgress();
				if (progress.getStatus() == Status.DONE) {
					if (progress.isMappingFailed()) setFailed();
					else setDone();
				}
			}
		});
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}
	
	protected void setDone() {
		configuration.setTitle("That's done");
		configuration.setButtons(PublishWizardStepButtons.NEW_PUBLISH);
		configuration.setSubtitle("Check the log for potential errors or warnings.");
		view.loadReport();
	}
	
	protected void setFailed() {
		configuration.setTitle("...Oops!");
		configuration.setButtons(PublishWizardStepButtons.BACKWARD);
		configuration.setSubtitle("Something went wrong, check the log.");
		view.loadReport();
	}
}
