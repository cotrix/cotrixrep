package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent.CodeListSelectedHandler;
import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationEditedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationEditedEvent.CsvParserConfigurationEditedHandler;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportStartedEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadFailedEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadedEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadingEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.NewImportEvent;
import org.cotrix.web.importwizard.client.event.NewImportEvent.NewImportHandler;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent.FileUploadedHandler;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.SaveEvent;
import org.cotrix.web.importwizard.client.event.SaveEvent.SaveHandler;
import org.cotrix.web.importwizard.client.session.ImportSession;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CodeListType;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.ImportProgress;

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
	
	@Inject
	protected ImportSession session;
	
	@Inject
	protected ImportServiceAsync importService;
	
	@Inject
	protected ImportWizardPresenter importWizardPresenter;
	
	protected ImportMetadata metadata;
	protected AttributesMappings mappings;
	
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
				selectedItemUpdated(event.getSelectedCodelist());
			}});

		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, new MetadataUpdatedHandler(){

			@Override
			public void onMetadataUpdated(MetadataUpdatedEvent event) {
				if (event.isUserEdited()) metadata = event.getMetadata();				
			}
		});
		importEventBus.addHandler(CsvParserConfigurationEditedEvent.TYPE, new CsvParserConfigurationEditedHandler(){

			@Override
			public void onCsvParserConfigurationEdited(CsvParserConfigurationEditedEvent event) {
				getMappings();
			}});
		importEventBus.addHandler(MappingsUpdatedEvent.TYPE, new MappingsUpdatedHandler() {
			
			@Override
			public void onMappingUpdated(MappingsUpdatedEvent event) {
				mappings = event.getMappings();
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
		
		Log.trace("getting mapping");
		getMappings();
		
		Log.trace("done importedItemUpdated");
	}
	
	protected void selectedItemUpdated(AssetInfo asset)
	{
		Log.trace("selectedItemUpdated");
		
		importEventBus.fireEvent(new CodeListTypeUpdatedEvent(asset.getCodeListType()));
		
		importService.setAsset(asset.getId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed setting the selected asset", caught);
			}

			@Override
			public void onSuccess(Void result) {
				Log.trace("asset selected on server");
				
				Log.trace("getting metadata");
				getMetadata();
				
				Log.trace("getting mapping");
				getMappings();
				
				Log.trace("done selectedItemUpdated");
			}
		});	
	}
	
	protected void getCodeListType(final Callback<CodeListType, Void> callaback)
	{
		importService.getCodeListType(new AsyncCallback<CodeListType>() {
			
			@Override
			public void onSuccess(CodeListType type) {
				Log.trace("retrieved codelist type "+type);
				importEventBus.fireEvent(new CodeListTypeUpdatedEvent(type));
				callaback.onSuccess(type);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed retrieving preview data", caught);
			}
		});
	}
	
	protected void getCsvParserConfiguration()
	{
		importService.getCsvParserConfiguration(new AsyncCallback<CsvParserConfiguration>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error retrieving CSV Parser configuration", caught);
			}

			@Override
			public void onSuccess(CsvParserConfiguration result) {
				Log.trace("parser configuration loaded: "+result);
				importEventBus.fireEvent(new CsvParserConfigurationUpdatedEvent(result));				
			}
		});
	}
	
	protected void getMetadata()
	{
		importService.getMetadata(new AsyncCallback<ImportMetadata>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error getting the Metadata", caught);
				
			}

			@Override
			public void onSuccess(ImportMetadata result) {
				importEventBus.fireEvent(new MetadataUpdatedEvent(result, false));
				metadata = result;
			}
		});
	}
	
	protected void getMappings()
	{
		Log.trace("getMappings");
		
		importEventBus.fireEvent(new MappingLoadingEvent());
		importService.getMappings(new AsyncCallback<AttributesMappings>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error getting the mappings", caught);
				importEventBus.fireEvent(new MappingLoadFailedEvent(caught));
			}

			@Override
			public void onSuccess(AttributesMappings result) {
				Log.trace("mapping retrieved");
				importEventBus.fireEvent(new MappingLoadedEvent(result));
				mappings = result;
			}
		});
	}
	
	protected void startImport()
	{
		Log.trace("starting import");
		importService.startImport(metadata, mappings, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error starting the import", caught);
			}

			@Override
			public void onSuccess(Void result) {
				importEventBus.fireEvent(new ImportStartedEvent());
				importProgressPolling.scheduleRepeating(1000);
			}
			
		});
	}
	
	protected void getImportProgress()
	{
		importService.getImportProgress(new AsyncCallback<ImportProgress>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error getting the import progress", caught);
			}

			@Override
			public void onSuccess(ImportProgress result) {
				Log.trace("Import progress: "+result);
				updateImportProgress(result);			
			}
		});
	}
	
	protected void updateImportProgress(ImportProgress progress)
	{
		if (progress.isComplete()) importProgressPolling.cancel();
		importEventBus.fireEvent(new ImportProgressEvent(progress));
		if (progress.isComplete()) importEventBus.fireEvent(NavigationEvent.FORWARD);
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
}
