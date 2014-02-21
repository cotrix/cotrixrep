package org.cotrix.web.ingest.client.step.codelistdetails;

import org.cotrix.web.common.client.error.ManagedFailureCallback;
import org.cotrix.web.ingest.client.ImportServiceAsync;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.AssetDetails;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.wizard.client.event.NavigationEvent;
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
public class CodelistDetailsStepPresenterImpl extends AbstractVisualWizardStep implements CodelistDetailsStepPresenter, ResetWizardHandler {

	protected final CodelistDetailsStepView view;
	
	@Inject
	protected ImportServiceAsync importService;
	
	protected EventBus importEventBus;
	
	protected AssetDetails visualizedAsset;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetails;
	
	@Inject
	public CodelistDetailsStepPresenterImpl(CodelistDetailsStepView view, @ImportBus EventBus importEventBus) {
		super("codelistDetails", TrackerLabels.ACQUIRE, "Codelist Details", "", ImportWizardStepButtons.BACKWARD);
		this.view = view;
		view.setPresenter(this);
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return true;
	}


	public void setAsset(AssetInfo asset) {
		Log.trace("getting asset details for "+asset);
		importService.getAssetDetails(asset.getId(), new ManagedFailureCallback<AssetDetails>() {
			
			@Override
			public void onSuccess(AssetDetails result) {
				view.setAssetDetails(result);
				visualizedAsset = result;
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
