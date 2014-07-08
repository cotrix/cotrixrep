package org.cotrix.web.ingest.client.step.upload;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.client.widgets.dialog.AlertDialog;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.FileUploadedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.resources.ImportConstants;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.FileUploadProgress;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;
import org.vectomatic.file.File;
import org.vectomatic.file.FileList;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class UploadStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, UploadStepView.Presenter, ResetWizardHandler {

	protected static final int POLLING_TIME = 500;
	protected static final int POLLING_ERROR_TRESHOLD = 3;
	
	@Inject
	protected IngestServiceAsync importService;
	
	protected EventBus importEventBus;
	
	protected Timer progressPolling;
	protected int pollingErrors = 0;
	
	protected boolean complete = false;
	
	protected UploadStepView view;
	protected File file;
	protected String fileName;
	
	@Inject
	private ImportConstants constants;
	
	@Inject
	private AlertDialog alertDialog;


	@Inject
	public UploadStepPresenter(UploadStepView view, @ImportBus EventBus importEventBus) {
		super("upload", TrackerLabels.ACQUIRE, "Upload it", "Choose any CSV or SDMX file.", ImportWizardStepButtons.BACKWARD, ImportWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		
		progressPolling = new Timer() {
			
			@Override
			public void run() {
				getUploadProgress();
			}
		};
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
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
		if (type == null || type.isEmpty()) {
			//we will check the type on server side
			return true;
		}
		
		
		if (contains(constants.csvMimeTypes(), type)) return true;
		if (contains(constants.xmlMimeTypes(), type)) return true;
		
		onError("Only CSV or SDMX for now");
		return false;
	}
	
	protected boolean contains(String[] array, String value)
	{
		for (String arrayValue:array) if (arrayValue.equals(value)) return true;
		return false;
	}
	
	protected void startUpload(File file)
	{
		complete = false;
		
		this.file = file;
		fileName = file.getName();
		view.setupUpload(fileName, file.getSize());
		
		importService.startUpload(new ManagedFailureCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				view.submitForm();
				
				//start polling
				pollingErrors = 0;
				progressPolling.scheduleRepeating(POLLING_TIME);
			}
		});
	}
	
	protected void getUploadProgress()
	{
		importService.getUploadProgress(new AsyncCallback<FileUploadProgress>() {
			
			@Override
			public void onSuccess(FileUploadProgress result) {
				Log.trace("progress "+result);
				updateProgress(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				pollingErrors++;
				Log.error("Failed getting upload progress", caught);
				if (pollingErrors>POLLING_ERROR_TRESHOLD) uploadFailed(Exceptions.toError(caught));
			}
		});
	}
	
	protected void updateProgress(FileUploadProgress progress) {
		Log.trace("updateProgress FileUploadProgress: "+progress);
		switch (progress.getStatus()) {
			case ONGOING: uploadOngoing(progress.getProgress()); break;
			case DONE: uploadDone(progress); break;
			case FAILED: uploadFailed(progress.getFailureCause()); break;
		}
	}
	
	protected void uploadOngoing(int progress)
	{
		view.setUploadProgress(progress);
	}
	
	protected void uploadDone(FileUploadProgress progress)
	{
		Log.trace("uploadDone");
		progressPolling.cancel();
		view.setUploadProgress(progress.getProgress());
		complete = true;
		importEventBus.fireEvent(new FileUploadedEvent(fileName, progress.getCodeListType()));
		view.setUploadComplete(progress.getCodeListType().toString());
	}
	
	protected void uploadFailed()
	{
		progressPolling.cancel();
		view.setUploadFailed();
	}
	
	protected void uploadFailed(org.cotrix.web.common.shared.Error error)
	{
		reset();
		alertDialog.center(error);
	}
	
	public void onDeleteButtonClicked() {
		reset();
	}

	public boolean leave() {
		return complete;
	}
	
	public void onError(String message) {
		view.alert(message);
	}

	public void onSubmitComplete(SubmitCompleteEvent event) {
		Log.trace("Submit complete");
	}

	@Override
	public void onRetryButtonClicked() {
		startUpload(file);	
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		reset();
	}
	
	protected void reset()
	{
		progressPolling.cancel();
		view.reset();
		complete = false;
		fileName = null;
	}

}
