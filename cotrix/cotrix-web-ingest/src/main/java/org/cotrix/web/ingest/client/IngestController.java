package org.cotrix.web.ingest.client;

import org.cotrix.web.common.client.CotrixModule;
import org.cotrix.web.common.client.CotrixModuleController;
import org.cotrix.web.common.client.Presenter;
import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.event.SwitchToModuleEvent;
import org.cotrix.web.common.shared.CsvConfiguration;
import org.cotrix.web.ingest.client.event.AssetRetrievedEvent;
import org.cotrix.web.ingest.client.event.CodeListSelectedEvent;
import org.cotrix.web.ingest.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.ingest.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.ingest.client.event.FileUploadedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.ManageEvent;
import org.cotrix.web.ingest.client.event.MetadataUpdatedEvent;
import org.cotrix.web.ingest.client.event.NewImportEvent;
import org.cotrix.web.ingest.client.event.RetrieveAssetEvent;
import org.cotrix.web.ingest.client.resources.Resources;
import org.cotrix.web.ingest.client.wizard.ImportWizardPresenter;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.ingest.shared.CodeListType;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Callback;
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
		importedItemUpdated(event.getCodeListType());
	}
	
	@EventHandler
	void onManage(ManageEvent event) {
		Log.trace("onManage");
		cotrixBus.fireEvent(new SwitchToModuleEvent(CotrixModule.MANAGE));
	}
	

	
	@EventHandler
	void onNewImport(NewImportEvent event) {
		importEventBus.fireEvent(new ResetWizardEvent());
	}
	
	@EventHandler
	void onRetrieveAsset(RetrieveAssetEvent event) {
		retrieveAsset();
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
