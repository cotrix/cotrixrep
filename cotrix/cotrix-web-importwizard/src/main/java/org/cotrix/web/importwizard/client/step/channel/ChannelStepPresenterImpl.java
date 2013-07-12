package org.cotrix.web.importwizard.client.step.channel;

import org.cotrix.web.importwizard.client.ImportServiceAsync;
import org.cotrix.web.importwizard.client.session.ImportSession;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.WizardStepConfiguration;
import org.cotrix.web.importwizard.shared.AssetInfo;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ChannelStepPresenterImpl implements ChannelStepPresenter {

	protected final ChannelStepView view;
	protected ImportServiceAsync importService;
	protected ImportSession session;
	protected AssetInfo assetInfo;
	
	@Inject
	public ChannelStepPresenterImpl(ChannelStepView view, ImportSession session, ImportServiceAsync importService){
		this.view = view;
		this.session = session;
		this.importService = importService;
		this.view.setPresenter(this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public WizardStepConfiguration getConfiguration() {
		return new WizardStepConfiguration("Channel", "Codelist selection", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
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
		
	}
}
