package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent.CodeListSelectedHandler;
import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationEditedEvent;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationEditedEvent.CsvParserConfigurationEditedHandler;
import org.cotrix.web.importwizard.client.event.CsvParserConfigurationUpdatedEvent;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent;
import org.cotrix.web.importwizard.client.event.PreviewDataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent.FileUploadedHandler;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.session.ImportSession;
import org.cotrix.web.importwizard.shared.CsvParserConfiguration;
import org.cotrix.web.importwizard.shared.CodeListPreviewData;
import org.cotrix.web.importwizard.shared.CodeListType;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class ImportWizardControllerImpl implements ImportWizardController {
	
	protected EventBus importEventBus;
	
	@Inject
	protected ImportSession session;
	
	@Inject
	protected ImportServiceAsync importService;
	
	@Inject
	protected ImportWizardPresenter importWizardPresenter;
	
	@Inject
	public ImportWizardControllerImpl(@ImportBus EventBus importEventBus)
	{
		this.importEventBus = importEventBus;
		bind();
	}
	
	protected void bind()
	{
		importEventBus.addHandler(FileUploadedEvent.TYPE, new FileUploadedHandler(){

			@Override
			public void onFileUploaded(FileUploadedEvent event) {
				importedItemUpdated();
			}});
		importEventBus.addHandler(CodeListSelectedEvent.TYPE, new CodeListSelectedHandler(){

			@Override
			public void onCodeListSelected(CodeListSelectedEvent event) {
				importedItemUpdated();
			}});
		importEventBus.addHandler(CsvParserConfigurationEditedEvent.TYPE, new CsvParserConfigurationEditedHandler(){

			@Override
			public void onCsvParserConfigurationEdited(
					CsvParserConfigurationEditedEvent event) {
				saveCsvParserConfiguration(event.getConfiguration());
			}});
	}
	
	protected void importedItemUpdated()
	{
		getPreviewData();
		getCodeListType(new Callback<CodeListType, Void>() {

			@Override
			public void onFailure(Void reason) {
			}

			@Override
			public void onSuccess(CodeListType result) {
				switch (result) {
					case CSV: getCSVParserConfiguration(); break;
					default: break;
				}				
			}
		});
	}
	
	protected void getPreviewData()
	{
		importService.getPreviewData(new AsyncCallback<CodeListPreviewData>() {
			
			@Override
			public void onSuccess(CodeListPreviewData previewData) {
				importEventBus.fireEvent(new PreviewDataUpdatedEvent(previewData));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed retrieving preview data", caught);
			}
		});
	}
	
	protected void getCodeListType(final Callback<CodeListType, Void> callaback)
	{
		importService.getCodeListType(new AsyncCallback<CodeListType>() {
			
			@Override
			public void onSuccess(CodeListType type) {
				importEventBus.fireEvent(new CodeListTypeUpdatedEvent(type));
				callaback.onSuccess(type);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed retrieving preview data", caught);
			}
		});
	}
	
	protected void getCSVParserConfiguration()
	{
		importService.getCsvParserConfiguration(new AsyncCallback<CsvParserConfiguration>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error retrieving CSV Parser configuration", caught);
			}

			@Override
			public void onSuccess(CsvParserConfiguration result) {
				importEventBus.fireEvent(new CsvParserConfigurationUpdatedEvent(result));				
			}
		});
	}
	
	protected void saveCsvParserConfiguration(CsvParserConfiguration configuration) {
		importService.updateCsvParserConfiguration(configuration, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error updating the CSV Parser configuration", caught);
			}

			@Override
			public void onSuccess(Void result) {
				getCSVParserConfiguration();
			}
		});
	}

	public void go(HasWidgets container) {
		importWizardPresenter.go(container);
	}
}
