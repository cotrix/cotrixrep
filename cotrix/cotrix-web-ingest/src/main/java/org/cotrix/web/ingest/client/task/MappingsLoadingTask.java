/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import java.util.Collections;
import java.util.List;

import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.AssetTypeUpdatedEvent;
import org.cotrix.web.ingest.client.event.CsvHeadersEvent;
import org.cotrix.web.ingest.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.MappingLoadFailedEvent;
import org.cotrix.web.ingest.client.event.MappingLoadedEvent;
import org.cotrix.web.ingest.client.wizard.ImportWizardAction;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.UIAssetType;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class MappingsLoadingTask implements TaskWizardStep {
	
	protected static interface MappingsLoadingTaskEventBinder extends EventBinder<MappingsLoadingTask> {}
	
	@Inject
	private IngestServiceAsync importService;
	
	@Inject
	@ImportBus
	private EventBus importEventBus;
	
	private List<String> userHeaders = Collections.emptyList();
	private List<AttributeMapping> lastMappings = null;
	
	
	@Inject
	private void bind(MappingsLoadingTaskEventBinder binder) {
		binder.bindEventHandlers(this, importEventBus);
	}
	
	@Inject
	private void bind()
	{
		importEventBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {

			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}});
	}

	@Override
	public String getId() {
		return "MappingsLoadingTask";
	}

	@Override
	public boolean leave() {
		return true;
	}
	
	@Override
	public void run(final TaskCallBack callback) {
		if (lastMappings == null) {
		importService.getMappings(userHeaders, new AsyncCallback<List<AttributeMapping>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error getting the mappings", caught);
				importEventBus.fireEvent(new MappingLoadFailedEvent(caught));
				callback.onFailure(Exceptions.toError(caught));
			}

			@Override
			public void onSuccess(List<AttributeMapping> result) {
				Log.trace("mapping retrieved");
				lastMappings = result;
				importEventBus.fireEvent(new MappingLoadedEvent(result));
				callback.onSuccess(ImportWizardAction.NEXT);
			}
		});
		} else callback.onSuccess(ImportWizardAction.NEXT);
	}	
	
	@EventHandler
	void onCsvHeaders(CsvHeadersEvent event) {
		userHeaders = event.getHeaders();
	}
	
	@EventHandler
	void onAssetTypeUpdated(AssetTypeUpdatedEvent event) {
		if (event.getAssetType() == UIAssetType.SDMX) userHeaders = Collections.emptyList();
		lastMappings = null;
	}
	
	@EventHandler
	void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {
		lastMappings = null;
	}
	
	private void reset() {
		userHeaders = Collections.emptyList();
		lastMappings = null;
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
