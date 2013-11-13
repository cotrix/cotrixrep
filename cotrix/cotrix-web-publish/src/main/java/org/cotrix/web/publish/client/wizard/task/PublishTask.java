/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import java.util.List;

import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.CodeListSelectedEvent;
import org.cotrix.web.publish.client.event.CsvWriterConfigurationUpdatedEvent;
import org.cotrix.web.publish.client.event.DestinationTypeChangeEvent;
import org.cotrix.web.publish.client.event.FormatTypeChangeEvent;
import org.cotrix.web.publish.client.event.MappingModeUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.event.PublishCompleteEvent;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.DestinationType;
import org.cotrix.web.publish.shared.DownloadType;
import org.cotrix.web.publish.shared.FormatType;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.PublishMetadata;
import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.step.TaskWizardStep;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.Progress;
import org.cotrix.web.share.shared.Progress.Status;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
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
	
	protected static final String DOWNLOAD_URL = GWT.getModuleBaseURL()+"publishDownload?"+DownloadType.PARAMETER_NAME+"="+DownloadType.CSV;
	
	@Inject
	protected PublishServiceAsync service;
	protected EventBus publishBus;
	protected AsyncCallback<WizardAction> callback;
	
	protected UICodelist codelist;
	protected DestinationType destination;
	protected FormatType type;
	protected List<AttributeMapping> mappings;
	protected CsvConfiguration csvConfiguration;
	protected MappingMode mappingMode;
	protected PublishMetadata metadata;
	
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
		
		publishBus.addHandler(CsvWriterConfigurationUpdatedEvent.TYPE, new CsvWriterConfigurationUpdatedEvent.CsvWriterConfigurationUpdatedHandler() {
			
			@Override
			public void onCsvWriterConfigurationUpdated(
					CsvWriterConfigurationUpdatedEvent event) {
				csvConfiguration = event.getConfiguration();
				
			}
		});
		
		publishBus.addHandler(MappingsUpdatedEvent.TYPE, new MappingsUpdatedEvent.MappingsUpdatedHandler() {
			
			@Override
			public void onMappingUpdated(MappingsUpdatedEvent event) {
				mappings = event.getMappings();
			}
		});
		
		publishBus.addHandler(MappingModeUpdatedEvent.TYPE, new MappingModeUpdatedEvent.MappingModeUpdatedHandler() {
			
			@Override
			public void onMappingModeUpdated(MappingModeUpdatedEvent event) {
				mappingMode = event.getMappingMode(); 
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
		PublishDirectives directives = new PublishDirectives();
		directives.setCodelistId(codelist.getId());
		directives.setCsvConfiguration(csvConfiguration);
		directives.setMappingMode(mappingMode);
		directives.setMetadata(metadata);
		directives.setMappings(mappings);
		
		Log.trace("PublishDirectives: "+directives);
		
		service.startPublish(directives, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("publication start failed", caught);
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
				if (result.isComplete()) publishComplete(result.getStatus());
				if (result.getStatus()==Status.DONE) startDownload();
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
	
	protected void publishComplete(Status status) {
		publishProgressPolling.cancel();
		publishBus.fireEvent(new PublishCompleteEvent(status));
		callback.onSuccess(PublishWizardAction.NEXT);
	}
	
	protected void startDownload() {
		Window.open(DOWNLOAD_URL, "myWindow", "");
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public WizardAction getAction() {
		return null;
	}

}
