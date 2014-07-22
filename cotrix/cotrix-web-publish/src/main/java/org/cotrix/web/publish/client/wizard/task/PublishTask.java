/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import org.cotrix.web.common.client.widgets.dialog.LoaderDialog;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.Format;
import org.cotrix.web.common.shared.Progress.Status;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.event.PublishCompleteEvent;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.publish.shared.DefinitionsMappings;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.publish.shared.DownloadType;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.publish.shared.PublishDirectives;
import org.cotrix.web.publish.shared.PublishMetadata;
import org.cotrix.web.publish.shared.PublishProgress;
import org.cotrix.web.publish.shared.UIRepository;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
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
	
	protected static final String DOWNLOAD_URL = GWT.getModuleBaseURL()+"service/publishDownload?"+DownloadType.PARAMETER_NAME+"="+DownloadType.RESULT;
	
	@Inject
	protected PublishServiceAsync service;
	@Inject
	private LoaderDialog loaderDialog;
	
	protected EventBus publishBus;
	protected TaskCallBack callback;
	
	protected UICodelist codelist;
	protected Destination destination;
	protected Format format;
	protected DefinitionsMappings mappings;
	protected CsvConfiguration csvConfiguration;
	protected UIQName repositoryId;
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
		publishBus.addHandler(ItemSelectedEvent.getType(UICodelist.class), new ItemSelectedEvent.ItemSelectedHandler<UICodelist>() {

			@Override
			public void onItemSelected(ItemSelectedEvent<UICodelist> event) {
				codelist = event.getItem();
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
		
		publishBus.addHandler(ItemUpdatedEvent.getType(CsvConfiguration.class), new ItemUpdatedEvent.ItemUpdatedHandler<CsvConfiguration>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<CsvConfiguration> event) {
				csvConfiguration = event.getItem();
			}
		});
		
		publishBus.addHandler(ItemSelectedEvent.getType(UIRepository.class), new ItemSelectedEvent.ItemSelectedHandler<UIRepository>() {

			@Override
			public void onItemSelected(ItemSelectedEvent<UIRepository> event) {
				repositoryId = event.getItem().getId();
			}
		});
		
		publishBus.addHandler(ItemUpdatedEvent.getType(PublishMetadata.class), new ItemUpdatedEvent.ItemUpdatedHandler<PublishMetadata>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<PublishMetadata> event) {
				metadata = event.getItem();
			}
		});
		
		publishBus.addHandler(ItemUpdatedEvent.getType(MappingMode.class), new ItemUpdatedEvent.ItemUpdatedHandler<MappingMode>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<MappingMode> event) {
				mappingMode = event.getItem();
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
	public void run(final TaskCallBack callback) {
		this.callback = callback;
		PublishDirectives directives = new PublishDirectives();
		directives.setCodelistId(codelist.getId());
		directives.setFormat(format);
		directives.setDestination(destination);
		directives.setCsvConfiguration(csvConfiguration);
		directives.setRepositoryId(repositoryId);
		directives.setMappingMode(mappingMode);
		directives.setMetadata(metadata);
		directives.setMappings(mappings);
		
		Log.trace("PublishDirectives: "+directives);
		
		loaderDialog.showCentered();
		
		service.startPublish(directives, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				publishProgressPolling.scheduleRepeating(1000);;
				
			}

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(Exceptions.toError(caught));
			}
		});
	}
	
	protected void getPublishProgress()
	{
		service.getPublishProgress(new AsyncCallback<PublishProgress>() {

			@Override
			public void onSuccess(PublishProgress result) {
				Log.trace("Import progress: "+result);
				if (result.isComplete()) publishComplete(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(Exceptions.toError(caught));
			}
		});
	}

	protected void reset() {
		callback = null;
		codelist = null;
		format = null;
		destination = null;
		mappings = null;
	}
	
	protected void publishComplete(PublishProgress progress) {
		publishProgressPolling.cancel();
		publishBus.fireEvent(new PublishCompleteEvent(progress, getDownloadUrl()));
		loaderDialog.hide();
		if (progress.getStatus() == Status.DONE) callback.onSuccess(PublishWizardAction.NEXT);
		else callback.onFailure(progress.getFailureCause());
	}
	
	private String getDownloadUrl() {
		if (destination != Destination.FILE) return null;
		String url = DOWNLOAD_URL;
		if (format!=null) url += "&" + Format.PARAMETER_NAME+"="+format;
		return url;
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
