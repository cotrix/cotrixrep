package org.cotrix.web.common.client.widgets.dialog;

import org.cotrix.web.common.client.CommonServiceAsync;
import org.cotrix.web.common.client.resources.CommonResources;
import org.cotrix.web.common.client.widgets.ProgressBar;
import org.cotrix.web.common.shared.LongTaskProgress;
import org.cotrix.web.common.shared.Progress.Status;
import org.cotrix.web.common.shared.exception.ServiceErrorException;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ProgressDialogImpl extends DialogBox implements ProgressDialog {
	
	private static final Binder binder = GWT.create(Binder.class);
	
	protected static final int POLLING_TIME = 1000;
	private static final int FAILURE_MAX = 3;
	
	@UiTemplate("ProgressDialog.ui.xml")
	interface Binder extends UiBinder<Widget, ProgressDialogImpl> {}
	
	@UiField
	ProgressBar progressBar;
	
	@UiField
	Label message;
	
	@Inject
	private CommonServiceAsync commonService;
	
	private Timer progressPolling;
	private int failureCounter = 0;
	
	private String progressToken;
	private ProgressCallBack callBack;
	
	private AsyncCallback<LongTaskProgress> asynCallBack = new AsyncCallback<LongTaskProgress>() {
		
		@Override
		public void onSuccess(LongTaskProgress result) {
			Log.trace("progress "+result);
			onProgressUpdate(result);
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Log.error("on progress retrieving failure", caught);
			onProgressRetrievingFailure(caught);
		}
	};

	public ProgressDialogImpl() {

		CommonResources.INSTANCE.css().ensureInjected();
		setGlassStyleName(CommonResources.INSTANCE.css().glassPanel());
		
		setModal(true);
		setGlassEnabled(true);
		setAnimationEnabled(true);
		
		setWidget(binder.createAndBindUi(this));
		
		progressBar.setMaxProgress(100);
		
		progressPolling = new Timer() {
			
			@Override
			public void run() {
				commonService.getProgress(progressToken, asynCallBack);
			}
		};
	}
	
	public void show(String progressToken) {
		show(progressToken, null);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void show(String progressToken, ProgressCallBack callBack) {
		Log.trace("show progressToken: "+progressToken);
		this.progressToken = progressToken;
		this.callBack = callBack;
		
		failureCounter = 0;
		progressPolling.scheduleRepeating(POLLING_TIME);
		progressBar.setProgress(0);
		
		super.center();
	}
	
	private void onProgressUpdate(LongTaskProgress progress) {
		progressBar.setProgress(progress.getPercentage());
		setMessage(progress.getMessage());
		
		if (progress.isComplete()) {
			progressPolling.cancel();
			if (progress.getStatus() == Status.DONE) progressBar.setProgress(100);
			hide();
			if (callBack!=null) {
				if (progress.getStatus() == Status.DONE) callBack.onSuccess(progress);
				else callBack.onFailure(new ServiceErrorException(progress.getFailureCause()));
			}
		}
	}
	
	private void setMessage(String text) {
		message.setText(text==null?"":text);
	}
	
	private void onProgressRetrievingFailure(Throwable caught) {
		if (failureCounter>FAILURE_MAX) {
			progressPolling.cancel();
			if (callBack!=null) callBack.onFailure(caught);
		} else failureCounter++;		
	}
	
}
