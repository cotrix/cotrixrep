/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import org.cotrix.web.publish.client.event.PublishProgressEvent;
import org.cotrix.web.publish.client.event.PublishProgressEvent.PublishProgressHandler;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.event.SaveEvent;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.share.client.wizard.step.TaskWizardStep;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class PublishTask implements TaskWizardStep, PublishProgressHandler, ResetWizardHandler {
	
	protected EventBus publishBus;
	protected AsyncCallback<WizardAction> callback;
	protected boolean publishComplete;
	
	@Inject
	public PublishTask(@PublishBus EventBus publishBus)
	{
		this.publishBus = publishBus;
		bind();
	}
	
	protected void bind()
	{
		publishBus.addHandler(PublishProgressEvent.TYPE, this);
		publishBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	@Override
	public String getId() {
		return "SaveTask";
	}

	@Override
	public boolean leave() {
		return publishComplete;
	}

	@Override
	public void run(AsyncCallback<WizardAction> callback) {
		this.callback = callback;
		publishBus.fireEvent(new SaveEvent());
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		callback = null;
		publishComplete = false;
	}

	@Override
	public void onPublishProgress(PublishProgressEvent event) {
		publishComplete = event.getProgress().isComplete();
		if (publishComplete) {
			callback.onSuccess(PublishWizardAction.NEXT);
		}
	}

}
