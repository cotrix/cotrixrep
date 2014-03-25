package org.cotrix.web.ingest.client.step.repositorydetails;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.common.shared.codelist.UIQName;
import org.cotrix.web.ingest.client.IngestServiceAsync;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class RepositoryDetailsStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, RepositoryDetailsStepView.Presenter, ResetWizardHandler {

	protected final RepositoryDetailsStepView view;
	
	@Inject
	protected IngestServiceAsync importService;
	
	protected EventBus importEventBus;
	
	protected AssetInfo selectedAsset;
	
	@Inject
	public RepositoryDetailsStepPresenter(RepositoryDetailsStepView view, @ImportBus EventBus importEventBus) {
		super("repositoryDetails", TrackerLabels.ACQUIRE, "Repository Details", "", ImportWizardStepButtons.BACKWARD);
		this.view = view;
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return false;
	}


	public void setRepository(UIQName repositoryId) {
		Log.trace("getting asset details for "+repositoryId);
		importService.getRepositoryDetails(repositoryId, new ManagedFailureCallback<RepositoryDetails>() {
			
			@Override
			public void onSuccess(RepositoryDetails result) {
				view.setRepository(result);
			}
		});
	}


	public void onResetWizard(ResetWizardEvent event) {

	}
}
