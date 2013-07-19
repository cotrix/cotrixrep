package org.cotrix.web.importwizard.client.step.upload;

import java.util.Arrays;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.UploadProgress;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class UploadStepPresenterImpl extends AbstractWizardStep implements UploadStepPresenter {

	protected static final String[] CSV_MIMETYPE = new String[]{"text/csv","text/plain"};
	protected static final String XML_MIMETYPE = "text/xml";
	protected static final int POLLING_TIME = 1000;
	protected static final int POLLING_ERROR_TRESHOLD = 3;
	
	@Inject
	protected ImportServiceAsync importService;
	
	@Inject
	@ImportBus 
	protected EventBus importEventBus;
	
	protected Timer progressPolling;
	protected int pollingErrors = 0;
	
	protected boolean complete = false;
	
	protected UploadStepView view;


	@Inject
	public UploadStepPresenterImpl(UploadStepView view) {
		super("upload", "Upload File", "Upload Codelist File", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		
		progressPolling = new Timer() {
			
			@Override
			public void run() {
				getUploadProgress();
			}
		};
	}
		
	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	

	public void onUploadFileChanged(FileList fileList, String filename) {
		Log.trace("UploadFileChanged");
		this.processFiles(fileList);
	}
	
	private void processFiles(FileList files) {
		Log.trace("processing selected files "+files.getLength());
		if (files.getLength() == 0)	return;

		File file = files.getItem(0);
		Log.trace("File name: "+file.getName()+" size: "+file.getSize()+" type: "+file.getType());
		
		boolean valid = isValid(file);
		if (valid) startUpload(file);
	}
	
	protected boolean isValid(File file)
	{
		if (file.getSize() == 0) {
			onError("The file looks empty");
			return false;
		}
		
		String type = file.getType();
		if (type == null) {
			//we will check the type on server side
			return true;
		}
		
		
		if (Arrays.binarySearch(CSV_MIMETYPE, type)>=0) return true;
		if (type.startsWith(XML_MIMETYPE)) return true;
		
		onError("The file should be a CSV or an SDMX file");
		return false;
	}
	
	protected void startUpload(File file)
	{
		complete = false;
		
		view.setupUpload(file.getName(), file.getSize());
		view.submitForm();
		
		//start polling
		pollingErrors = 0;
		progressPolling.scheduleRepeating(POLLING_TIME);
	}
	
	protected void getUploadProgress()
	{
		importService.getUploadProgress(new AsyncCallback<UploadProgress>() {
			
			@Override
			public void onSuccess(UploadProgress result) {
				updateProgress(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				pollingErrors++;
				Log.error("Failed getting upload progress", caught);
				if (pollingErrors>POLLING_ERROR_TRESHOLD) uploadFailed();
			}
		});
	}
	
	protected void updateProgress(UploadProgress progress) {
		Log.trace("updateProgress "+progress);
		switch (progress.getStatus()) {
			case ONGOING: uploadOngoing(progress.getProgress()); break;
			case DONE: uploadDone(); break;
			case FAILED: uploadFailed(); break;
		}
	}
	
	protected void uploadOngoing(int progress)
	{
		view.setUploadProgress(progress);
	}
	
	protected void uploadDone()
	{
		view.setUploadComplete();
		progressPolling.cancel();
		complete = true;
		importEventBus.fireEvent(new FileUploadedEvent());
	}
	
	protected void uploadFailed()
	{
		view.setUploadFailed();
		progressPolling.cancel();
	}
	
	public void onDeleteButtonClicked() {
		progressPolling.cancel();
		//view.resetFileUpload();
		view.reset();
		complete = false;
	}

	public boolean isComplete() {
		return complete;
	}
	
	public void onError(String message) {
		view.alert(message);
	}

	public void onSubmitComplete(SubmitCompleteEvent event) {

	}

	@Override
	public void onRetryButtonClicked() {
		// TODO Auto-generated method stub
		
	}

}
