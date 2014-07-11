/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.AssetRetrievedEvent;
import org.cotrix.web.ingest.client.event.AssetSelectedEvent;
import org.cotrix.web.ingest.client.event.AssetTypeUpdatedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.wizard.ImportWizardAction;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import static org.cotrix.web.common.client.async.AsyncUtils.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class RetrieveAssetTask implements TaskWizardStep, ResetWizardHandler {
	
	protected static interface RetrieveAssetTaskEventBinder extends EventBinder<RetrieveAssetTask> {}
	
	protected EventBus importEventBus;
	protected boolean assetRetrieved;
	protected AssetInfo selectedAsset;

	@Inject
	protected IngestServiceAsync importService;
	
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
	public void run(final TaskCallBack callback) {
		Log.trace("retrieveAsset");
		importEventBus.fireEvent(new AssetTypeUpdatedEvent(selectedAsset.getAssetType()));

		
		importService.setAsset(selectedAsset.getId(), showLoader(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed setting the selected asset", caught);
				callback.onFailure(Exceptions.toError(caught));
			}

			@Override
			public void onSuccess(Void result) {
				Log.trace("asset selected on server");
				

				Log.trace("done selectedItemUpdated");
				
				assetRetrieved = true;
				importEventBus.fireEvent(new AssetRetrievedEvent(selectedAsset));
				
				callback.onSuccess(ImportWizardAction.NEXT);
			}
		}));	
	}
	
	@Override
	public void onResetWizard(ResetWizardEvent event) {
		assetRetrieved = false;
	}	
	
	@EventHandler
	void onCodeListSelected(AssetSelectedEvent event) {
		selectedAsset = event.getSelectedAsset();
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
