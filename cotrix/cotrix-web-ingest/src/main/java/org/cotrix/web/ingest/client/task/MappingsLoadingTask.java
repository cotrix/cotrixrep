/**
 * 
 */
package org.cotrix.web.ingest.client.task;

import java.util.List;

import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.MappingLoadFailedEvent;
import org.cotrix.web.ingest.client.event.MappingLoadedEvent;
import org.cotrix.web.ingest.client.wizard.ImportWizardAction;
import org.cotrix.web.ingest.shared.AttributeMapping;
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
	protected IngestServiceAsync importService;
	
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
	public void run(final TaskCallBack callback) {
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
