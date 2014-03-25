/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import java.util.List;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.event.CodeListImportedEvent;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.Progress.Status;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.ImportProgressEvent;
import org.cotrix.web.ingest.client.event.MappingLoadedEvent;
import org.cotrix.web.ingest.client.event.MappingModeUpdatedEvent;
import org.cotrix.web.ingest.client.event.MappingsUpdatedEvent;
import org.cotrix.web.ingest.client.event.MetadataUpdatedEvent;
import org.cotrix.web.ingest.client.wizard.ImportWizardAction;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.MappingMode;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Timer;
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

	protected EventBus importEventBus;
	protected TaskCallBack callback;
	protected boolean importComplete;



	@Inject
	protected IngestServiceAsync importService;

	@Inject @CotrixBus
	protected EventBus cotrixBus;

	protected CsvConfiguration csvConfiguration;
	protected ImportMetadata metadata;
	protected List<AttributeMapping> mappings;
	protected MappingMode mappingMode;	

	protected Timer importProgressPolling;


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

	@Inject
	private void setupTimer()
	{
		importProgressPolling = new Timer() {

			@Override
			public void run() {
				getImportProgress();
			}
		};
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
		importService.startImport(csvConfiguration, metadata, mappings, mappingMode, new ManagedFailureCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				importProgressPolling.scheduleRepeating(1000);
			}

		});
	}

	protected void getImportProgress()
	{
		importService.getImportProgress(new ManagedFailureCallback<Progress>() {

			@Override
			public void onSuccess(Progress result) {
				Log.trace("Import progress: "+result);
				updateImportProgress(result);			
			}
		});
	}

	protected void updateImportProgress(Progress progress)
	{
		importComplete = progress.isComplete();
		importEventBus.fireEvent(new ImportProgressEvent(progress));
		if (importComplete) codelistImportComplete(progress);
	}

	protected void codelistImportComplete(Progress progress)
	{
		Log.trace("CodeList import complete");
		importProgressPolling.cancel();
		//FIXME add id
		cotrixBus.fireEvent(new CodeListImportedEvent(null));
		if (progress.getStatus() == Status.DONE) callback.onSuccess(ImportWizardAction.NEXT);
		else callback.onFailure(progress.getFailureCause());
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		callback = null;
		importComplete = false;
		metadata = null;
		mappings = null;
		importProgressPolling.cancel();
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
