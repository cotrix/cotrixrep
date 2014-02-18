package org.cotrix.web.importwizard.client.step.repositorydetails;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.step.TrackerLabels;
import org.cotrix.web.importwizard.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.share.client.error.ManagedFailureCallback;
import org.cotrix.web.share.shared.codelist.RepositoryDetails;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryDetailsStepPresenterImpl extends AbstractVisualWizardStep implements RepositoryDetailsStepPresenter, ResetWizardHandler {

	protected final RepositoryDetailsStepView view;
	
	@Inject
	protected ImportServiceAsync importService;
	
	protected EventBus importEventBus;
	
	protected AssetInfo selectedAsset;
	
	@Inject
	public RepositoryDetailsStepPresenterImpl(RepositoryDetailsStepView view, @ImportBus EventBus importEventBus) {
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


	public void setRepository(String repositoryId) {
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
