package org.cotrix.web.importwizard.client.step.channel;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;

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
public class ChannelStepPresenterImpl extends AbstractWizardStep implements ChannelStepPresenter {

	protected final ChannelStepView view;
	
	@Inject
	protected ImportServiceAsync importService;
	
	@Inject
	@ImportBus
	protected EventBus importEventBus;
	
	protected AssetInfo assetInfo;
	
	@Inject
	public ChannelStepPresenterImpl(ChannelStepView view) {
		super("channel", "Channel", "Codelist selection", DEFAULT_BACKWARD, DEFAULT_FORWARD);
		this.view = view;
		this.view.setPresenter(this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return assetInfo!=null;
	}

	@Override
	public void assetSelected(AssetInfo asset) {
		this.assetInfo = asset;
		importEventBus.fireEvent(new CodeListSelectedEvent());
	}

	@Override
	public void assetDetails(AssetInfo asset) {
		importService.getAssetDetails(asset.getId(), new AsyncCallback<AssetDetails>() {
			
			@Override
			public void onSuccess(AssetDetails result) {
				view.showAssetDetails(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed loading asset details", caught);
			}
		});
	}
}
