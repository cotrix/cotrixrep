/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import static org.cotrix.web.common.client.async.AsyncUtils.*;

import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.AssetDetailsEvent;
import org.cotrix.web.ingest.client.event.AssetDetailsRequestEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.wizard.ImportWizardAction;
import org.cotrix.web.ingest.shared.AssetDetails;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.allen_sauer.gwt.log.client.Log;
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
public class RetrieveAssetDetailsTask implements TaskWizardStep {
	
	protected static interface RetrieveAssetDetailsTaskEventBinder extends EventBinder<RetrieveAssetDetailsTask> {}
	
	@Inject
	protected IngestServiceAsync service;
	
	protected AssetInfo asset;
	
	protected EventBus importBus;
	
	@Inject
	protected void bind(RetrieveAssetDetailsTaskEventBinder binder, @ImportBus EventBus importBus)
	{
		binder.bindEventHandlers(this, importBus);
		
		this.importBus = importBus;
		importBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}
		});
	}
	
	@EventHandler
	protected void onAssetDetailsRequest(AssetDetailsRequestEvent event) {
		Log.trace("onAssetDetailsRequest asset: " +event.getAsset());
		this.asset = event.getAsset();
	}

	@Override
	public String getId() {
		return "RetrieveAssetDetailsTask";
	}

	@Override
	public boolean leave() {
		return true;
	}

	@Override
	public void run(final TaskCallBack callback) {
		Log.trace("retrieving details for asset "+asset);
		service.getAssetDetails(asset.getId(), showLoader(new AsyncCallback<AssetDetails>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Details retrieving failed", caught);
				callback.onFailure(Exceptions.toError(caught));
			}
			
			@Override
			public void onSuccess(AssetDetails result) {
				Log.trace("asset details: "+result);
				importBus.fireEvent(new AssetDetailsEvent(result));
				callback.onSuccess(ImportWizardAction.NEXT);
			}
		}));
	}
	
	public void reset() {
		asset = null;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WizardAction getAction() {
		// TODO Auto-generated method stub
		return null;
	}

}
