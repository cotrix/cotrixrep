/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import static org.cotrix.web.common.client.async.AsyncUtils.*;

import java.util.List;

import org.cotrix.web.common.client.event.CodeListImportedEvent;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.ingest.client.AsyncIngestServiceAsync;
import org.cotrix.web.ingest.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.ImportResultEvent;
import org.cotrix.web.ingest.client.event.MappingLoadedEvent;
import org.cotrix.web.ingest.client.event.MappingModeUpdatedEvent;
import org.cotrix.web.ingest.client.event.MappingsUpdatedEvent;
import org.cotrix.web.ingest.client.event.MetadataUpdatedEvent;
import org.cotrix.web.ingest.client.wizard.ImportWizardAction;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.ImportResult;
import org.cotrix.web.ingest.shared.MappingMode;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
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
public class ImportTask implements TaskWizardStep, ResetWizardHandler {

	protected static interface ImportTaskEventBinder extends EventBinder<ImportTask> {}

	private EventBus importEventBus;
	private TaskCallBack callback;
	private boolean importComplete;
	
	@Inject
	private AsyncIngestServiceAsync asyncImportService;
	
	@Inject @CotrixBus
	private EventBus cotrixBus;

	private CsvConfiguration csvConfiguration;
	private ImportMetadata metadata;
	private List<AttributeMapping> mappings;
	private MappingMode mappingMode;	


	@Inject
	public ImportTask(@ImportBus EventBus importEventBus)
	{
		this.importEventBus = importEventBus;
		bind();
	}

	@Inject
	private void bind(ImportTaskEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
	}

	protected void bind()
	{
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	@Override
	public String getId() {
		return "SaveTask";
	}

	@Override
	public boolean leave() {
		return importComplete;
	}

	@Override
	public void run(TaskCallBack callback) {
		this.callback = callback;

		Log.trace("starting import");
		
		asyncImportService.startImport(csvConfiguration, metadata, mappings, mappingMode, async(
				
				new AsyncCallback<ImportResult>() {

			@Override
			public void onSuccess(ImportResult result) {
				importComplete(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				importFailed(caught);
			}
		}));
	}
	
	private void importComplete(ImportResult result) {
		Log.trace("importComplete result: "+result);
		importComplete = true;
		importEventBus.fireEvent(new ImportResultEvent(result));
		if (!result.isMappingFailed()) cotrixBus.fireEvent(new CodeListImportedEvent(result.getCodelistId()));
		callback.onSuccess(ImportWizardAction.NEXT);
	}
	
	private void importFailed(Throwable caught) {
		callback.onFailure(Exceptions.toError(caught));
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		callback = null;
		importComplete = false;
		metadata = null;
		mappings = null;
	}

	@EventHandler
	void onMappingLoaded(MappingLoadedEvent event) {
		mappings = event.getMappings();
	}

	@EventHandler
	void onMappingModeUpdated(MappingModeUpdatedEvent event) {
		mappingMode = event.getMappingMode();
	}

	@EventHandler
	void onMappingUpdated(MappingsUpdatedEvent event) {
		mappings = event.getMappings();
	}

	@EventHandler
	void onMetadataUpdated(MetadataUpdatedEvent event) {
		metadata = event.getMetadata();				
	}

	@EventHandler
	void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {
		csvConfiguration = event.getConfiguration();
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
