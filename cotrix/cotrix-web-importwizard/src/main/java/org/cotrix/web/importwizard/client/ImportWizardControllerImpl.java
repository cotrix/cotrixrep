package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent.CodeListSelectedHandler;
import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent;
import org.cotrix.web.importwizard.client.event.PreviewDataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent.FileUploadedHandler;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.session.ImportSession;
import org.cotrix.web.importwizard.shared.CodeListPreviewData;
import org.cotrix.web.importwizard.shared.CodeListType;

import com.allen_sauer.gwt.log.client.Log;
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
				getPreviewData();
				getCodeListType();
			}});
		importEventBus.addHandler(CodeListSelectedEvent.TYPE, new CodeListSelectedHandler(){

			@Override
			public void onCodeListSelected(CodeListSelectedEvent event) {
				getPreviewData();
				getCodeListType();
			}});
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
	
	protected void getCodeListType()
	{
		importService.getCodeListType(new AsyncCallback<CodeListType>() {
			
			@Override
			public void onSuccess(CodeListType type) {
				importEventBus.fireEvent(new CodeListTypeUpdatedEvent(type));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed retrieving preview data", caught);
			}
		});
	}

	public void go(HasWidgets container) {
		importWizardPresenter.go(container);
	}
}
