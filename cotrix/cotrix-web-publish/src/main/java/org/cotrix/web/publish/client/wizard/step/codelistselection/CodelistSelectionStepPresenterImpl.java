package org.cotrix.web.publish.client.wizard.step.codelistselection;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.share.client.wizard.event.NavigationEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;
import org.cotrix.web.share.shared.UICodelist;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CodelistSelectionStepPresenterImpl extends AbstractVisualWizardStep implements CodelistSelectionStepPresenter, ResetWizardHandler {

	protected final CodelistSelectionStepView view;
	
	/*@Inject
	protected DetailsNodeSelector detailsNodeSelector;*/
	
	/*@Inject
	protected CodelistDetailsStepPresenter codelistDetailsPresenter;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetailsPresenter;*/
	
	protected EventBus publishEventBus;
	
	protected UICodelist selectedCodelist;
	
	@Inject
	public CodelistSelectionStepPresenterImpl(CodelistSelectionStepView view, @PublishBus EventBus publishEventBus) {
		super("selection", TrackerLabels.SELECTION, "Pick a codelist", "We found a few nearby.", PublishWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		this.publishEventBus = publishEventBus;
		publishEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		/*Log.trace("SelectionStep leaving: "+(detailsNodeSelector.toDetails() || selectedAsset!=null));
		return detailsNodeSelector.toDetails() || selectedAsset!=null;*/
		return true;
	}

	@Override
	public void codelistSelected(UICodelist codelist) {
		Log.trace("Codelist selected "+codelist);
		if (selectedCodelist!=null && selectedCodelist.equals(codelist)) return;
		
		this.selectedCodelist = codelist;
		//importEventBus.fireEvent(new CodeListSelectedEvent(selectedAsset));
	}

	@Override
	public void codelistDetails(UICodelist codelist) {
		
		/*codelistDetailsPresenter.setAsset(asset);
		detailsNodeSelector.switchToCodeListDetails();*/
		publishEventBus.fireEvent(NavigationEvent.FORWARD);
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		view.reset();
	}

	@Override
	public void repositoryDetails(String repositoryId) {
		/*repositoryDetailsPresenter.setRepository(repositoryId);
		detailsNodeSelector.switchToRepositoryDetails();*/
		publishEventBus.fireEvent(NavigationEvent.FORWARD);
	}
}
