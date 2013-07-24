package org.cotrix.web.importwizard.client.step.channel;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;

import org.cotrix.web.importwizard.shared.AssetDetails;
import org.cotrix.web.importwizard.shared.AssetInfo;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import static org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ChannelStepPresenterImpl extends AbstractWizardStep implements ChannelStepPresenter, ResetWizardHandler {

	protected final ChannelStepView view;
	
	@Inject
	protected ImportServiceAsync importService;
	
	protected EventBus importEventBus;
	
	protected AssetInfo assetInfo;
	
	@Inject
	public ChannelStepPresenterImpl(ChannelStepView view, @ImportBus EventBus importEventBus) {
		super("channel", "Channel", "Codelist selection", DEFAULT_BACKWARD, DEFAULT_FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		return assetInfo!=null;
	}

	@Override
	public void assetSelected(AssetInfo asset) {
		Log.trace("Asset selected "+asset);
		this.assetInfo = asset;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				importEventBus.fireEvent(new CodeListSelectedEvent());
			}
		});
	}

	@Override
	public void assetDetails(AssetInfo asset) {
		Log.trace("getting asset details for "+asset);
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

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		view.reset();
	}
}
