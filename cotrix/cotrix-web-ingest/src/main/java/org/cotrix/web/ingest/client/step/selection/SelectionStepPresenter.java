package org.cotrix.web.ingest.client.step.selection;

import org.cotrix.web.ingest.client.event.CodeListSelectedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.DetailsNodeSelector;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.ingest.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.AssetInfo;
import org.cotrix.web.wizard.client.event.NavigationEvent;
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
public class SelectionStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, SelectionStepView.Presenter, ResetWizardHandler {

	protected final SelectionStepView view;
	
	@Inject
	protected DetailsNodeSelector detailsNodeSelector;
	
	@Inject
	protected CodelistDetailsStepPresenter codelistDetailsPresenter;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetailsPresenter;
	
	protected EventBus importEventBus;
	
	protected AssetInfo selectedAsset;
	
	@Inject
	public SelectionStepPresenter(SelectionStepView view, @ImportBus EventBus importEventBus) {
		super("selection", TrackerLabels.ACQUIRE, "Pick a codelist", "We found a few nearby.", ImportWizardStepButtons.BACKWARD, ImportWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		Log.trace("SelectionStep leaving: "+(detailsNodeSelector.toDetails() || selectedAsset!=null));
		return detailsNodeSelector.toDetails() || selectedAsset!=null;
	}

	@Override
	public void assetSelected(AssetInfo asset) {
		Log.trace("Asset selected "+asset);
		if (selectedAsset!=null && selectedAsset.equals(asset)) return;
		
		this.selectedAsset = asset;
		importEventBus.fireEvent(new CodeListSelectedEvent(selectedAsset));
	}

	@Override
	public void assetDetails(AssetInfo asset) {
		/*Log.trace("getting asset details for "+asset);
		importService.getAssetDetails(asset.getId(), new AsyncCallback<AssetDetails>() {
			
			@Override
			public void onSuccess(AssetDetails result) {
				view.showAssetDetails(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading asset details", caught);
			}
		});*/
		
		codelistDetailsPresenter.setAsset(asset);
		detailsNodeSelector.switchToCodeListDetails();
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		view.reset();
	}

	@Override
	public void repositoryDetails(String repositoryId) {
		repositoryDetailsPresenter.setRepository(repositoryId);
		detailsNodeSelector.switchToRepositoryDetails();
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}
}
