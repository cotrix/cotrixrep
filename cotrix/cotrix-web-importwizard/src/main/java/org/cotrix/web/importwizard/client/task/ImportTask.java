/**
 * 
 */
package org.cotrix.web.importwizard.client.task;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.SaveEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.wizard.ImportWizardAction;
import org.cotrix.web.share.client.wizard.WizardAction;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.share.client.wizard.step.TaskWizardStep;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ImportTask implements TaskWizardStep, ImportProgressHandler, ResetWizardHandler {
	
	protected EventBus importEventBus;
	protected AsyncCallback<WizardAction> callback;
	protected boolean importComplete;
	
	@Inject
	public ImportTask(@ImportBus EventBus importEventBus)
	{
		this.importEventBus = importEventBus;
		bind();
	}
	
	protected void bind()
	{
		importEventBus.addHandler(ImportProgressEvent.TYPE, this);
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

	@Override
	public void onImportProgress(ImportProgressEvent event) {
		importComplete = event.getProgress().isComplete();
		if (importComplete) {
			callback.onSuccess(ImportWizardAction.NEXT);
		}
	}

}
