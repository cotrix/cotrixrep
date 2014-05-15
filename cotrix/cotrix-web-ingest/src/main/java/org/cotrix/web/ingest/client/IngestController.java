package org.cotrix.web.ingest.client;

import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;
import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.SwitchToModuleEvent;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.client.event.AssetRetrievedEvent;
import org.cotrix.web.ingest.client.event.AssetTypeUpdatedEvent;
import org.cotrix.web.ingest.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.ingest.client.event.FileUploadedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.ManageEvent;
import org.cotrix.web.ingest.client.event.MetadataUpdatedEvent;
import org.cotrix.web.ingest.client.event.NewImportEvent;
import org.cotrix.web.ingest.client.resources.Resources;
import org.cotrix.web.ingest.client.wizard.ImportWizardPresenter;
import org.cotrix.web.ingest.shared.UIAssetType;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Callback;
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

	@Inject
	private Resources resources;
	
	@Inject
	private void setupCss() {
		resources.css().ensureInjected();
	}
	
	@Inject
	protected void bind(ImportWizardControllerBinder binder, @ImportBus EventBus importEventBus)
	{
		this.importEventBus = importEventBus;
		binder.bindEventHandlers(this, importEventBus);
	}
	
	@EventHandler
	void onFileUploaded(FileUploadedEvent event) {
		importedItemUpdated(event.getAssetType());
	}
	
	@EventHandler
	void onManage(ManageEvent event) {
		Log.trace("onManage");
		cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.MANAGE));
	}
	
	@EventHandler
	void onAssetRetrieved(AssetRetrievedEvent event) {
		importedItemUpdated(event.getAsset().getAssetType());
	}
	
	@EventHandler
	void onNewImport(NewImportEvent event) {
		importEventBus.fireEvent(new ResetWizardEvent());
	}

	protected void importedItemUpdated(UIAssetType assetType)
	{
		Log.trace("importedItemUpdated assetType: "+assetType);

		importEventBus.fireEvent(new AssetTypeUpdatedEvent(assetType));

		if (assetType == UIAssetType.CSV) {
			Log.trace("getting parser configuration");
			getCsvParserConfiguration(); 
		}

		Log.trace("getting metadata");
		getMetadata();

		Log.trace("done importedItemUpdated");
	}

	protected void getCodeListType(final Callback<UIAssetType, Void> callaback)
	{
		importService.getCodeListType(new ManagedFailureCallback<UIAssetType>() {

			@Override
			public void onSuccess(UIAssetType type) {
				Log.trace("retrieved codelist type "+type);
				importEventBus.fireEvent(new AssetTypeUpdatedEvent(type));
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
			}
		});
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
		importEventBus.fireEvent(new ResetWizardEvent());
	}

	@Override
	public void deactivate() {
	}
}
