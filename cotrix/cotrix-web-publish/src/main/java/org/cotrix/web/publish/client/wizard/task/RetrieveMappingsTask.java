/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import java.util.List;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.DestinationType;
import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.step.TaskWizardStep;
import org.cotrix.web.share.shared.codelist.UICodelist;

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
public class RetrieveMappingsTask implements TaskWizardStep {
	
	@Inject
	protected PublishServiceAsync service;
	
	protected UICodelist selectedCodelist;
	protected DestinationType destinationType;
	
	protected EventBus publishBus;
	
	@Inject
	public RetrieveMappingsTask(@PublishBus EventBus publishBus)
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
		publishBus.addHandler(ItemUpdatedEvent.getType(DestinationType.class), new ItemUpdatedEvent.ItemUpdatedHandler<DestinationType>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<DestinationType> event) {
				destinationType = event.getItem();
			}
		});
	}

	@Override
	public String getId() {
		return "RetrieveMappingsTask";
	}

	@Override
	public boolean leave() {
		return true;
	}

	@Override
	public void run(final AsyncCallback<WizardAction> callback) {
		Log.trace("retrieving mappings for codelist "+selectedCodelist);
		service.getMappings(selectedCodelist.getId(), destinationType, new AsyncCallback<List<AttributeMapping>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<AttributeMapping> result) {
				publishBus.fireEventFromSource(new MappingsUpdatedEvent(result),  RetrieveMappingsTask.this);
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
