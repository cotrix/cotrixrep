/**
 * 
 */
package org.cotrix.web.importwizard.client.task;

import java.util.List;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingLoadFailedEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadedEvent;
import org.cotrix.web.importwizard.client.wizard.ImportWizardAction;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class MappingsLoadingTask implements TaskWizardStep {
	
	@Inject
	protected ImportServiceAsync importService;
	
	@Inject
	@ImportBus
	protected EventBus importEventBus;

	@Override
	public String getId() {
		return "MappingsLoadingTask";
	}

	@Override
	public boolean leave() {
		return true;
	}
	
	@Override
	public void run(final AsyncCallback<WizardAction> callback) {
		importService.getMappings(new AsyncCallback<List<AttributeMapping>>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error getting the mappings", caught);
				importEventBus.fireEvent(new MappingLoadFailedEvent(caught));
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<AttributeMapping> result) {
				Log.trace("mapping retrieved");
				importEventBus.fireEvent(new MappingLoadedEvent(result));
				callback.onSuccess(ImportWizardAction.NEXT);
			}
		});
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
