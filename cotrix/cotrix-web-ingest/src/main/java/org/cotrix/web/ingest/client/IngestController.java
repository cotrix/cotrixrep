package org.cotrix.web.ingest.client;

import java.util.List;

import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;
import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.event.CodeListImportedEvent;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.SwitchToModuleEvent;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.AssetRetrievedEvent;
import org.cotrix.web.ingest.client.event.CodeListSelectedEvent;
import org.cotrix.web.ingest.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.ingest.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.ingest.client.event.FileUploadedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.ImportProgressEvent;
import org.cotrix.web.ingest.client.event.ManageEvent;
import org.cotrix.web.ingest.client.event.MappingLoadedEvent;
import org.cotrix.web.ingest.client.event.MappingModeUpdatedEvent;
import org.cotrix.web.ingest.client.event.MappingsUpdatedEvent;
import org.cotrix.web.ingest.client.event.MetadataUpdatedEvent;
import org.cotrix.web.ingest.client.event.NewImportEvent;
import org.cotrix.web.ingest.client.event.RetrieveAssetEvent;
import org.cotrix.web.ingest.client.event.SaveEvent;
import org.cotrix.web.ingest.client.resources.Resources;
import org.cotrix.web.ingest.client.wizard.ImportWizardPresenter;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.CodeListType;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.MappingMode;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
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
public class IngestController implements Presenter, CotrixModuleController {
	
	protected interface ImportWizardControllerBinder extends EventBinder<IngestController> {}

	protected EventBus importEventBus;
	
	@Inject @CotrixBus
	protected EventBus cotrixBus;

	@Inject
	protected IngestServiceAsync importService;

	@Inject
	protected ImportWizardPresenter importWizardPresenter;

	protected AssetInfo selectedAsset;

	protected ImportMetadata metadata;
	protected List<AttributeMapping> mappings;
	protected MappingMode mappingMode;
	protected CsvConfiguration csvConfiguration;

	protected Timer importProgressPolling;
	
	@Inject
	private Resources resources;
	
	@Inject
	private void setupCss() {
		resources.css().ensureInjected();
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
	
	@Inject
	protected void bind(ImportWizardControllerBinder binder, @ImportBus EventBus importEventBus)
	{
		this.importEventBus = importEventBus;
		binder.bindEventHandlers(this, importEventBus);
	}
	
	@EventHandler
	void onFileUploaded(FileUploadedEvent event) {
		importedItemUpdated(event.getCodeListType());
	}
	
	@EventHandler
	void onManage(ManageEvent event) {
		Log.trace("onManage");
		cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.MANAGE));
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
		if (event.isUserEdited()) metadata = event.getMetadata();				
	}
	
	@EventHandler
	void onNewImport(NewImportEvent event) {
		newImportRequested();
	}
	
	@EventHandler
	void onRetrieveAsset(RetrieveAssetEvent event) {
		retrieveAsset();
	}
	
	@EventHandler
	void onSave(SaveEvent event) {
		startImport();
	}
	
	@EventHandler
	void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {
		csvConfiguration = event.getConfiguration();
	}
	
	@EventHandler
	void onCodeListSelected(CodeListSelectedEvent event) {
		selectedAsset = event.getSelectedCodelist();
	}

	protected void importedItemUpdated(CodeListType codeListType)
	{
		Log.trace("importedItemUpdated codeListType: "+codeListType);

		importEventBus.fireEvent(new CodeListTypeUpdatedEvent(codeListType));

		if (codeListType == CodeListType.CSV) {
			Log.trace("getting parser configuration");
			getCsvParserConfiguration(); 
		}

		Log.trace("getting metadata");
		getMetadata();

		Log.trace("done importedItemUpdated");
	}

	protected void retrieveAsset()
	{
		Log.trace("retrieveAsset");
		importEventBus.fireEvent(new CodeListTypeUpdatedEvent(selectedAsset.getCodeListType()));

		importService.setAsset(selectedAsset.getId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed setting the selected asset", caught);
				//FIXME
			}

			@Override
			public void onSuccess(Void result) {
				Log.trace("asset selected on server");
				
				Log.trace("getting metadata");
				getMetadata();

				Log.trace("done selectedItemUpdated");
				importEventBus.fireEvent(new AssetRetrievedEvent());
			}
		});	
	}

	protected void getCodeListType(final Callback<CodeListType, Void> callaback)
	{
		importService.getCodeListType(new ManagedFailureCallback<CodeListType>() {

			@Override
			public void onSuccess(CodeListType type) {
				Log.trace("retrieved codelist type "+type);
				importEventBus.fireEvent(new CodeListTypeUpdatedEvent(type));
				callaback.onSuccess(type);
			}

		});
	}

	protected void getCsvParserConfiguration()
	{
		importService.getCsvParserConfiguration(new ManagedFailureCallback<CsvConfiguration>() {

			@Override
			public void onSuccess(CsvConfiguration result) {
				Log.trace("parser configuration loaded: "+result);
				csvConfiguration = result;
				importEventBus.fireEventFromSource(new CsvParserConfigurationUpdatedEvent(result), IngestController.this);				
			}
		});
	}

	protected void getMetadata()
	{
		importService.getMetadata(new ManagedFailureCallback<ImportMetadata>() {

			@Override
			public void onSuccess(ImportMetadata result) {
				importEventBus.fireEvent(new MetadataUpdatedEvent(result, false));
				metadata = result;
			}
		});
	}

	protected void startImport()
	{
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
		if (progress.isComplete()) codelistImportComplete();
		importEventBus.fireEvent(new ImportProgressEvent(progress));
	}
	
	protected void codelistImportComplete()
	{
		Log.trace("CodeList import complete");
		importProgressPolling.cancel();
		//FIXME add id
		cotrixBus.fireEvent(new CodeListImportedEvent(null));
	}

	protected void newImportRequested()
	{
		metadata = null;
		mappings = null;
		importProgressPolling.cancel();
		importEventBus.fireEvent(new ResetWizardEvent());
	}

	public void go(HasWidgets container) {
		importWizardPresenter.go(container);
	}

	@Override
	public CotrixModule getModule() {
		return CotrixModule.IMPORT;
	}

	@Override
	public void activate() {
		newImportRequested();
	}

	@Override
	public void deactivate() {
	}
}
