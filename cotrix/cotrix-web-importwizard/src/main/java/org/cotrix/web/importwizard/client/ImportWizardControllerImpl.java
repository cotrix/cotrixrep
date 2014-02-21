package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.event.AssetRetrievedEvent;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent.CodeListSelectedHandler;
import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent.FileUploadedHandler;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportStartedEvent;
import org.cotrix.web.importwizard.client.event.ManageEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadedEvent;
import org.cotrix.web.importwizard.client.event.MappingModeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.event.NewImportEvent;
import org.cotrix.web.importwizard.client.event.NewImportEvent.NewImportHandler;
import org.cotrix.web.importwizard.client.event.RetrieveAssetEvent;
import org.cotrix.web.importwizard.client.event.SaveEvent;
import org.cotrix.web.importwizard.client.event.SaveEvent.SaveHandler;
import org.cotrix.web.importwizard.client.wizard.ImportWizardPresenter;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;
import org.cotrix.web.share.client.CotrixModule;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.client.event.CodeListImportedEvent;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.SwitchToModuleEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.shared.CsvConfiguration;
import org.cotrix.web.share.shared.Progress;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportWizardControllerImpl implements ImportWizardController {

	protected EventBus importEventBus;
	
	@Inject @CotrixBus
	protected EventBus cotrixBus;

	@Inject
	protected ImportServiceAsync importService;

	@Inject
	protected ImportWizardPresenter importWizardPresenter;

	protected AssetInfo selectedAsset;

	protected ImportMetadata metadata;
	protected List<AttributeMapping> mappings;
	protected MappingMode mappingMode;
	protected CsvConfiguration csvConfiguration;

	protected Timer importProgressPolling;

	@Inject
	public ImportWizardControllerImpl(@ImportBus EventBus importEventBus)
	{
		this.importEventBus = importEventBus;

		importProgressPolling = new Timer() {

			@Override
			public void run() {
				getImportProgress();
			}
		};

		bind();
	}

	protected void bind()
	{
		importEventBus.addHandler(FileUploadedEvent.TYPE, new FileUploadedHandler(){

			@Override
			public void onFileUploaded(FileUploadedEvent event) {
				importedItemUpdated(event.getCodeListType());
			}});

		importEventBus.addHandler(CodeListSelectedEvent.TYPE, new CodeListSelectedHandler(){

			@Override
			public void onCodeListSelected(CodeListSelectedEvent event) {
				selectedAsset = event.getSelectedCodelist();
			}});

		importEventBus.addHandler(RetrieveAssetEvent.TYPE, new RetrieveAssetEvent.RetrieveAssetHandler() {

			@Override
			public void onRetrieveAsset(RetrieveAssetEvent event) {
				retrieveAsset();
			}
		});

		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, new MetadataUpdatedHandler(){

			@Override
			public void onMetadataUpdated(MetadataUpdatedEvent event) {
				if (event.isUserEdited()) metadata = event.getMetadata();				
			}
		});
		importEventBus.addHandler(MappingsUpdatedEvent.TYPE, new MappingsUpdatedHandler() {

			@Override
			public void onMappingUpdated(MappingsUpdatedEvent event) {
				mappings = event.getMappings();
			}
		});
		importEventBus.addHandler(MappingModeUpdatedEvent.TYPE, new MappingModeUpdatedEvent.MappingModeUpdatedHandler() {

			@Override
			public void onMappingModeUpdated(MappingModeUpdatedEvent event) {
				mappingMode = event.getMappingMode();
			}
		});
		importEventBus.addHandler(SaveEvent.TYPE, new SaveHandler() {

			@Override
			public void onSave(SaveEvent event) {
				startImport();
			}
		});
		importEventBus.addHandler(NewImportEvent.TYPE, new NewImportHandler(){

			@Override
			public void onNewImport(NewImportEvent event) {
				newImportRequested();
			}});
		
		importEventBus.addHandler(ManageEvent.TYPE, new ManageEvent.ManageHandler() {
			
			@Override
			public void onManage(ManageEvent event) {
				cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.MANAGE));
			}
		});
		importEventBus.addHandler(MappingLoadedEvent.TYPE, new MappingLoadedEvent.MappingLoadedHandler() {
			
			@Override
			public void onMappingLoaded(MappingLoadedEvent event) {
				mappings = event.getMappings();
			}
		});
		
		importEventBus.addHandler(CsvParserConfigurationUpdatedEvent.TYPE, new CsvParserConfigurationUpdatedEvent.CsvParserConfigurationUpdatedHandler() {
			
			@Override
			public void onCsvParserConfigurationUpdated(CsvParserConfigurationUpdatedEvent event) {
				csvConfiguration = event.getConfiguration();
				
			}
		});
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
				importEventBus.fireEventFromSource(new CsvParserConfigurationUpdatedEvent(result), ImportWizardControllerImpl.this);				
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
				importEventBus.fireEvent(new ImportStartedEvent());
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
