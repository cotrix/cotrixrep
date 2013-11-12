/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import java.util.List;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.CodeListSelectedEvent;
import org.cotrix.web.publish.client.event.DestinationType;
import org.cotrix.web.publish.client.event.DestinationTypeChangeEvent;
import org.cotrix.web.publish.client.event.FormatType;
import org.cotrix.web.publish.client.event.FormatTypeChangeEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.step.TaskWizardStep;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class PublishTask implements TaskWizardStep {
	
	@Inject
	protected PublishServiceAsync service;
	protected EventBus publishBus;
	protected AsyncCallback<WizardAction> callback;
	
	protected UICodelist codelist;
	protected DestinationType destination;
	protected FormatType type;
	protected List<AttributeMapping> mappings;
	
	protected Timer publishProgressPolling;
	
	@Inject
	public PublishTask(@PublishBus EventBus publishBus)
	{
		this.publishBus = publishBus;
		
		publishProgressPolling = new Timer() {

			@Override
			public void run() {
				getPublishProgress();
			}
		};
		
		bind();
	}
	
	protected void bind()
	{
		publishBus.addHandler(CodeListSelectedEvent.TYPE, new CodeListSelectedEvent.CodeListSelectedHandler() {
			
			@Override
			public void onCodeListSelected(CodeListSelectedEvent event) {
				codelist = event.getSelectedCodelist();
			}
		});
		
		publishBus.addHandler(DestinationTypeChangeEvent.TYPE, new DestinationTypeChangeEvent.DestinationTypeChangeHandler() {
			
			@Override
			public void onDestinationTypeChange(DestinationTypeChangeEvent event) {
				destination = event.getDestinationType();
			}
		});
		
		publishBus.addHandler(FormatTypeChangeEvent.TYPE, new FormatTypeChangeEvent.FormatTypeChangeHandler() {
			
			@Override
			public void onFormatTypeChange(FormatTypeChangeEvent event) {
				type = event.getFormatType();
			}
		});
		
		publishBus.addHandler(MappingsUpdatedEvent.TYPE, new MappingsUpdatedEvent.MappingsUpdatedHandler() {
			
			@Override
			public void onMappingUpdated(MappingsUpdatedEvent event) {
				mappings = event.getMappings();
			}
		});
	}
	

	@Override
	public String getId() {
		return "PublishTask";
	}

	@Override
	public boolean leave() {
		return true;
	}

	@Override
	public void run(AsyncCallback<WizardAction> callback) {
		this.callback = callback;
		service.startPublish(codelist.getId(), mappings, null, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Void result) {
				publishProgressPolling.scheduleRepeating(1000);;
				
			}
		});
	}
	
	protected void getPublishProgress()
	{
		service.getPublishProgress(new AsyncCallback<Progress>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error getting the import progress", caught);
			}

			@Override
			public void onSuccess(Progress result) {
				Log.trace("Import progress: "+result);
				if (result.isComplete()) publishComplete();
			}
		});
	}

	protected void reset() {
		callback = null;
		codelist = null;
		type = null;
		destination = null;
		mappings = null;
	}
	
	protected void publishComplete() {
		publishProgressPolling.cancel();
		callback.onSuccess(PublishWizardAction.NEXT);
	}

}
