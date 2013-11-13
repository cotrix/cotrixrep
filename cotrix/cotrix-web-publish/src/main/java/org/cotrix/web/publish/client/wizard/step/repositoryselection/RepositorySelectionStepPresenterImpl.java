package org.cotrix.web.publish.client.wizard.step.repositoryselection;

import org.cotrix.web.publish.client.event.CodeListSelectedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositorySelectionStepPresenterImpl extends AbstractVisualWizardStep implements RepositorySelectionStepPresenter, ResetWizardHandler {

	protected final RepositorySelectionStepView view;
	
	/*@Inject
	protected DetailsNodeSelector detailsNodeSelector;*/
	
	/*@Inject
	protected CodelistDetailsStepPresenter codelistDetailsPresenter;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetailsPresenter;*/
	
	protected EventBus publishEventBus;
	
	protected UICodelist selectedCodelist;
	
	@Inject
	public RepositorySelectionStepPresenterImpl(RepositorySelectionStepView view, @PublishBus EventBus publishEventBus) {
		super("repository", TrackerLabels.TARGET, "Pick your target", "We found a few repositories nearby.", PublishWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		this.publishEventBus = publishEventBus;
		publishEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		Log.trace("SelectionStep leaving: "+(selectedCodelist!=null));
		return selectedCodelist!=null;
	}

	@Override
	public void codelistSelected(UICodelist codelist) {
		Log.trace("Codelist selected "+codelist);
		if (selectedCodelist!=null && selectedCodelist.equals(codelist)) return;
		
		this.selectedCodelist = codelist;
		publishEventBus.fireEvent(new CodeListSelectedEvent(codelist));
	}

	@Override
	public void codelistDetails(UICodelist codelist) {
		
		/*codelistDetailsPresenter.setAsset(asset);
		detailsNodeSelector.switchToCodeListDetails();
		publishEventBus.fireEvent(NavigationEvent.FORWARD);*/
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		view.reset();
	}

	@Override
	public void repositoryDetails(String repositoryId) {
		/*repositoryDetailsPresenter.setRepository(repositoryId);
		detailsNodeSelector.switchToRepositoryDetails();
		publishEventBus.fireEvent(NavigationEvent.FORWARD);*/
	}
}
