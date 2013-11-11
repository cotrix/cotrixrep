/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.CodeListSelectedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.step.TaskWizardStep;
import org.cotrix.web.share.shared.codelist.CodelistMetadata;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RetrieveMetadataTask implements TaskWizardStep {
	
	@Inject
	protected PublishServiceAsync service;
	protected AsyncCallback<WizardAction> callback;
	protected UICodelist selectedCodelist;
	
	@Inject
	public RetrieveMetadataTask(@PublishBus EventBus publishBus)
	{
		bind(publishBus);
	}
	
	protected void bind(EventBus publishBus)
	{
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}
		});
		publishBus.addHandler(CodeListSelectedEvent.TYPE, new CodeListSelectedEvent.CodeListSelectedHandler(){

			@Override
			public void onCodeListSelected(CodeListSelectedEvent event) {
				selectedCodelist = event.getSelectedCodelist();
			}
			
		});
	}

	@Override
	public String getId() {
		return "RetrieveCodelistTask";
	}

	@Override
	public boolean leave() {
		return true;
	}

	@Override
	public void run(final AsyncCallback<WizardAction> callback) {
		this.callback = callback;
		service.getMetadata(selectedCodelist.getId(), new AsyncCallback<CodelistMetadata>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(CodelistMetadata result) {
				callback.onSuccess(PublishWizardAction.NEXT);
			}
		});
	}
	
	public void reset() {
		callback = null;
		selectedCodelist = null;
	}

}
