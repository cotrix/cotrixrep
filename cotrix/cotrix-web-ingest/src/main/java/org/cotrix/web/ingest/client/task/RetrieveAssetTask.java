/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import org.cotrix.web.ingest.client.event.AssetRetrievedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.RetrieveAssetEvent;
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
public class RetrieveAssetTask implements TaskWizardStep, ResetWizardHandler {
	
	protected static interface RetrieveAssetTaskEventBinder extends EventBinder<RetrieveAssetTask> {}
	
	protected EventBus importEventBus;
	protected AsyncCallback<WizardAction> callback;
	protected boolean assetRetrieved;
	
	@Inject
	protected void bind(RetrieveAssetTaskEventBinder binder, @ImportBus EventBus importEventBus)
	{
		binder.bindEventHandlers(this, importEventBus);
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	@Override
	public String getId() {
		return "RetrieveAssetTask";
	}

	@Override
	public boolean leave() {
		return assetRetrieved;
	}

	@Override
	public void run(AsyncCallback<WizardAction> callback) {
		this.callback = callback;
		importEventBus.fireEvent(new RetrieveAssetEvent());
	}
	
	@Override
	public void onResetWizard(ResetWizardEvent event) {
		callback = null;
		assetRetrieved = false;
	}

	@EventHandler
	void onAssetRetrieved(AssetRetrievedEvent event) {
		assetRetrieved = true;
		callback.onSuccess(ImportWizardAction.NEXT);
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
