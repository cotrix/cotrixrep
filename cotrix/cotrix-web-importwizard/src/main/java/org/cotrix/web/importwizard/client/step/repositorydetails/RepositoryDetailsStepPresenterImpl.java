package org.cotrix.web.importwizard.client.step.repositorydetails;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;

import org.cotrix.web.importwizard.shared.AssetInfo;
import org.cotrix.web.importwizard.shared.RepositoryDetails;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import static org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryDetailsStepPresenterImpl extends AbstractWizardStep implements RepositoryDetailsStepPresenter, ResetWizardHandler {

	protected final RepositoryDetailsStepView view;
	
	@Inject
	protected ImportServiceAsync importService;
	
	protected EventBus importEventBus;
	
	protected AssetInfo selectedAsset;
	
	@Inject
	public RepositoryDetailsStepPresenterImpl(RepositoryDetailsStepView view, @ImportBus EventBus importEventBus) {
		super("repositoryDetails", TrackerLabels.ACQUIRE, "Repository Details", "", BACKWARD);
		this.view = view;
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return false;
	}


	public void setRepository(String repositoryId) {
		Log.trace("getting asset details for "+repositoryId);
		importService.getRepositoryDetails(repositoryId, new AsyncCallback<RepositoryDetails>() {
			
			@Override
			public void onSuccess(RepositoryDetails result) {
				view.setRepository(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading asset details", caught);
			}
		});
	}


	public void onResetWizard(ResetWizardEvent event) {

	}
}
