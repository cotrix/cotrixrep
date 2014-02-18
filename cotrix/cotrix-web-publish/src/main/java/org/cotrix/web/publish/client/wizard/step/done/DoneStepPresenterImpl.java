package org.cotrix.web.publish.client.wizard.step.done;

import org.cotrix.web.publish.client.event.PublishCompleteEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DoneStepPresenterImpl extends AbstractVisualWizardStep implements DoneStepPresenter {
	
	protected DoneStepView view;
	protected EventBus publishBus;
	
	@Inject
	public DoneStepPresenterImpl(DoneStepView view, @PublishBus EventBus publishBus) {
		super("done", TrackerLabels.DONE, "Done", "Done", PublishWizardStepButtons.NEW_PUBLISH);
		this.view = view;
		this.publishBus = publishBus;
		bind();
	}
	
	protected void bind() {
		publishBus.addHandler(PublishCompleteEvent.TYPE, new PublishCompleteEvent.PublishCompleteHandler() {
			
			@Override
			public void onPublishComplete(PublishCompleteEvent event) {
				switch (event.getStatus()) {
					case DONE: setDone(); break;
					case FAILED: setFailed(); break;
					default:;
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
