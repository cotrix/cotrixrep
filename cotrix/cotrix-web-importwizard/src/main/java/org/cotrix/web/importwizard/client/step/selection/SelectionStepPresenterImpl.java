package org.cotrix.web.importwizard.client.step.selection;

import org.cotrix.web.importwizard.client.DetailsNodeSelector;
import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.importwizard.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;

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
public class SelectionStepPresenterImpl extends AbstractWizardStep implements SelectionStepPresenter, ResetWizardHandler {

	protected final SelectionStepView view;
	
	@Inject
	protected ImportServiceAsync importService;
	
	@Inject
	protected DetailsNodeSelector detailsNodeSelector;
	
	@Inject
	protected CodelistDetailsStepPresenter codelistDetailsPresenter;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetailsPresenter;
	
	protected EventBus importEventBus;
	
	protected AssetInfo selectedAsset;
	
	@Inject
	public SelectionStepPresenterImpl(SelectionStepView view, @ImportBus EventBus importEventBus) {
		super("selection", TrackerLabels.ACQUIRE, "Pick a codelist", "", DEFAULT_BACKWARD, DEFAULT_FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return detailsNodeSelector.toDetails() || selectedAsset!=null;
	}

	@Override
	public void assetSelected(AssetInfo asset) {
		Log.trace("Asset selected "+asset);
		if (selectedAsset!=null && selectedAsset.equals(asset)) return;
		
		this.selectedAsset = asset;
		importService.setAsset(asset.getId(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed setting the selected asset", caught);
			}

			@Override
			public void onSuccess(Void result) {
				Log.trace("asset selected on server, firing event");
				importEventBus.fireEvent(new CodeListSelectedEvent());
			}
		});
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
