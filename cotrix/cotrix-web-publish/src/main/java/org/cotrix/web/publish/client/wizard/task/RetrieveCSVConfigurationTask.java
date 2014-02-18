/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.codelist.UICodelist;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class RetrieveCSVConfigurationTask implements TaskWizardStep {
	
	@Inject
	protected PublishServiceAsync service;
	
	protected UICodelist selectedCodelist;
	
	protected EventBus publishBus;
	
	@Inject
	public RetrieveCSVConfigurationTask(@PublishBus EventBus publishBus)
	{
		this.publishBus = publishBus;
		bind();
	}
	
	protected void bind()
	{
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}
		});
		
		publishBus.addHandler(ItemSelectedEvent.getType(UICodelist.class), new ItemSelectedEvent.ItemSelectedHandler<UICodelist>() {

			@Override
			public void onItemSelected(ItemSelectedEvent<UICodelist> event) {
				selectedCodelist = event.getItem();
			}
			
		});
	}

	@Override
	public String getId() {
		return "RetrieveCsvConfigurationTask";
	}

	@Override
	public boolean leave() {
		return true;
	}

	@Override
	public void run(final AsyncCallback<WizardAction> callback) {
		Log.trace("retrieving CsvParserConfiguratio for codelist "+selectedCodelist);
		service.getCsvWriterConfiguration(selectedCodelist.getId(), new AsyncCallback<CsvConfiguration>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
				
			}

			@Override
			public void onSuccess(CsvConfiguration result) {
				publishBus.fireEventFromSource(new ItemUpdatedEvent<CsvConfiguration>(result), RetrieveCSVConfigurationTask.this);
				callback.onSuccess(PublishWizardAction.NEXT);
			}
		});
	}
	
	public void reset() {
		selectedCodelist = null;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WizardAction getAction() {
		// TODO Auto-generated method stub
		return null;
	}

}
