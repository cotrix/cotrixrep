/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import org.cotrix.web.common.shared.Progress;
import org.cotrix.web.common.shared.Progress.Status;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.ImportProgressEvent;
import org.cotrix.web.ingest.client.event.SaveEvent;
import org.cotrix.web.ingest.client.wizard.ImportWizardAction;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
public class ImportTask implements TaskWizardStep, ResetWizardHandler {
	
	protected static interface ImportTaskEventBinder extends EventBinder<ImportTask> {}
	
	protected EventBus importEventBus;
	protected AsyncCallback<WizardAction> callback;
	protected boolean importComplete;
	
	@Inject
	public ImportTask(@ImportBus EventBus importEventBus)
	{
		this.importEventBus = importEventBus;
		bind();
	}
	
	@Inject
	private void bind(ImportTaskEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
	}
	
	protected void bind()
	{
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	@Override
	public String getId() {
		return "SaveTask";
	}

	@Override
	public boolean leave() {
		return importComplete;
	}

	@Override
	public void run(AsyncCallback<WizardAction> callback) {
		this.callback = callback;
		importEventBus.fireEvent(new SaveEvent());
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		callback = null;
		importComplete = false;
	}

	@EventHandler
	void onImportProgress(ImportProgressEvent event) {
		Progress progress = event.getProgress();
		importComplete = progress.isComplete();
		if (importComplete) {
			if (progress.getStatus() == Status.DONE) callback.onSuccess(ImportWizardAction.NEXT);
			else callback.onFailure(progress.getFailureCause());
		}
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public WizardAction getAction() {
		return null;
	}

}
