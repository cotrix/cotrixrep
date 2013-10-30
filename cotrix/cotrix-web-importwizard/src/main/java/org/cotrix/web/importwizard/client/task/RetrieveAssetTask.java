/**
 * 
 */
package org.cotrix.web.importwizard.client.task;

import org.cotrix.web.importwizard.client.ImportWizardAction;
import org.cotrix.web.importwizard.client.event.AssetRetrievedEvent;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.AssetRetrievedEvent.AssetRetrievedHandler;
import org.cotrix.web.importwizard.client.event.RetrieveAssetEvent;
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
public class RetrieveAssetTask implements TaskWizardStep, ResetWizardHandler, AssetRetrievedHandler  {
	
	protected EventBus importEventBus;
	protected AsyncCallback<WizardAction> callback;
	protected boolean assetRetrieved;
	
	@Inject
	public RetrieveAssetTask(@ImportBus EventBus importEventBus)
	{
		this.importEventBus = importEventBus;
		bind();
	}
	
	protected void bind()
	{
		importEventBus.addHandler(AssetRetrievedEvent.TYPE, this);
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

	@Override
	public void onAssetRetrieved(AssetRetrievedEvent event) {
		assetRetrieved = true;
		callback.onSuccess(ImportWizardAction.NEXT);
	}

}
