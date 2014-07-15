/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import static org.cotrix.web.common.client.async.AsyncUtils.*;

import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.RepositoryDetailsEvent;
import org.cotrix.web.ingest.client.event.RepositoryDetailsRequestEvent;
import org.cotrix.web.ingest.client.wizard.ImportWizardAction;
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
public class RetrieveRepositoryDetailsTask implements TaskWizardStep {
	
	protected static interface RetrieveRepositoryDetailsTaskEventBinder extends EventBinder<RetrieveRepositoryDetailsTask> {}
	
	@Inject
	protected IngestServiceAsync service;
	
	protected UIQName repositoryId;
	
	protected EventBus importBus;
	
	@Inject
	protected void bind(RetrieveRepositoryDetailsTaskEventBinder binder, @ImportBus EventBus importBus)
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
	protected void onRepositorySelected(RepositoryDetailsRequestEvent event) {
		Log.trace("onRepositorySelected repositoryId: " +event.getRepositoryId());
		this.repositoryId = event.getRepositoryId();
	}

	@Override
	public String getId() {
		return "RetrieveRepositoryDetailsTask";
	}

	@Override
	public boolean leave() {
		return true;
	}

	@Override
	public void run(final TaskCallBack callback) {
		Log.trace("retrieving details for repository "+repositoryId);
		service.getRepositoryDetails(repositoryId, showLoader(new AsyncCallback<RepositoryDetails>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Details retrieving failed", caught);
				callback.onFailure(Exceptions.toError(caught));
			}

			@Override
			public void onSuccess(RepositoryDetails result) {
				Log.trace("repository details: "+result);
				importBus.fireEvent(new RepositoryDetailsEvent(result));
				callback.onSuccess(ImportWizardAction.NEXT);
			}
		}));
	}
	
	public void reset() {
		repositoryId = null;
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
