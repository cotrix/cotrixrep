/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.common.shared.exception.Exceptions;
import org.cotrix.web.publish.client.PublishServiceAsync;
import org.cotrix.web.publish.client.event.ItemDetailsRequestedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.publish.shared.UIRepository;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import static org.cotrix.web.common.client.async.AsyncUtils.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class RetrieveRepositoryDetailsTask implements TaskWizardStep {
	
	@Inject
	protected PublishServiceAsync service;
	
	protected UIRepository selectedRepository;
	
	protected EventBus publishBus;
	
	@Inject
	public RetrieveRepositoryDetailsTask(@PublishBus EventBus publishBus)
	{
		this.publishBus = publishBus;
		bind(publishBus);
	}
	
	protected void bind(EventBus publishBus)
	{
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}
		});
		publishBus.addHandler(ItemDetailsRequestedEvent.getType(UIRepository.class), new ItemDetailsRequestedEvent.ItemDetailsRequestedHandler<UIRepository>() {

			@Override
			public void onItemDetailsRequest(ItemDetailsRequestedEvent<UIRepository> event) {
				selectedRepository = event.getItem();
			}
		});
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
		Log.trace("retrieving details for repository "+selectedRepository);
		service.getRepositoryDetails(selectedRepository.getId(), showLoader(new AsyncCallback<RepositoryDetails>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Details retrieving failed", caught);
				callback.onFailure(Exceptions.toError(caught));
			}

			@Override
			public void onSuccess(RepositoryDetails result) {
				publishBus.fireEvent(new ItemUpdatedEvent<RepositoryDetails>(result));
				callback.onSuccess(PublishWizardAction.NEXT);
			}
		}));
	}
	
	public void reset() {
		selectedRepository = null;
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
