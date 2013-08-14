package org.cotrix.web.importwizard.client.step.codelistdetails;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;

import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;

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
public class CodelistDetailsStepPresenterImpl extends AbstractWizardStep implements CodelistDetailsStepPresenter, ResetWizardHandler {

	protected final CodelistDetailsStepView view;
	
	@Inject
	protected ImportServiceAsync importService;
	
	protected EventBus importEventBus;
	
	protected AssetDetails visualizedAsset;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetails;
	
	@Inject
	public CodelistDetailsStepPresenterImpl(CodelistDetailsStepView view, @ImportBus EventBus importEventBus) {
		super("codelistDetails", TrackerLabels.ACQUIRE, "Codelist Details", "", DEFAULT_BACKWARD, NONE);
		this.view = view;
		view.setPresenter(this);
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return true;
	}


	public void setAsset(AssetInfo asset) {
		Log.trace("getting asset details for "+asset);
		importService.getAssetDetails(asset.getId(), new AsyncCallback<AssetDetails>() {
			
			@Override
			public void onSuccess(AssetDetails result) {
				view.setAssetDetails(result);
				visualizedAsset = result;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading asset details", caught);
			}
		});
	}


	public void onResetWizard(ResetWizardEvent event) {

	}

	@Override
	public void repositoryDetails() {
		repositoryDetails.setRepository(visualizedAsset.getRepositoryId());
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}
}
