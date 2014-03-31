/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import java.util.List;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.publish.shared.Format;
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
public class RetrieveMappingsTask implements TaskWizardStep {
	
	@Inject
	protected PublishServiceAsync service;
	
	protected UICodelist selectedCodelist;
	protected Destination destination;
	protected Format format;
	
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
		
		publishBus.addHandler(ItemUpdatedEvent.getType(Destination.class), new ItemUpdatedEvent.ItemUpdatedHandler<Destination>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<Destination> event) {
				destination = event.getItem();
			}
		});
		
		publishBus.addHandler(ItemUpdatedEvent.getType(Format.class), new ItemUpdatedEvent.ItemUpdatedHandler<Format>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<Format> event) {
				format = event.getItem();
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
	public void run(final TaskCallBack callback) {
		Log.trace("retrieving mappings for codelist "+selectedCodelist+" destinationType: "+destination+" format: "+format);
		service.getMappings(selectedCodelist.getId(),  destination, format, new AsyncCallback<List<AttributeMapping>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Mapping retrieving failed", caught);
				callback.onFailure(Exceptions.toError(caught));
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
