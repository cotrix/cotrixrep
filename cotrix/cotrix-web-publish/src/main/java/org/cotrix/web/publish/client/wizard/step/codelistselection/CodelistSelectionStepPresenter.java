package org.cotrix.web.publish.client.wizard.step.codelistselection;

import org.cotrix.web.common.shared.codelist.LifecycleState;
import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.DetailsNodeSelector;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.client.wizard.task.RetrieveMetadataTask;
import org.cotrix.web.publish.shared.PublishMetadata;
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
public class CodelistSelectionStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, CodelistSelectionStepView.Presenter, ResetWizardHandler {

	protected final CodelistSelectionStepView view;
	
	@Inject
	protected DetailsNodeSelector detailsNodeSelector;
	
	@Inject
	protected RetrieveMetadataTask retrieveMetadataTask;
	
	protected EventBus publishEventBus;
	
	protected UICodelist selectedCodelist;
	
	@Inject
	public CodelistSelectionStepPresenter(CodelistSelectionStepView view, @PublishBus EventBus publishEventBus) {
		super("selection", TrackerLabels.SELECTION, "Which one do we publish?", "Pick a codelist.", PublishWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		this.publishEventBus = publishEventBus;
		publishEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		Log.trace("CodelistSelectionStep leaving");
		if (selectedCodelist!=null && !detailsNodeSelector.isSwitchedToCodeListDetails()) {
			publishEventBus.fireEvent(new ItemSelectedEvent<UICodelist>(selectedCodelist));
			PublishMetadata metadata = new PublishMetadata();
			metadata.setName(selectedCodelist.getName());
			metadata.setOriginalName(selectedCodelist.getName());
			metadata.setVersion(selectedCodelist.getVersion());
			metadata.setSealed(selectedCodelist.getState() == LifecycleState.sealed);
			publishEventBus.fireEvent(new ItemUpdatedEvent<PublishMetadata>(metadata));
		}
		return selectedCodelist!=null || detailsNodeSelector.isSwitchedToCodeListDetails();
	}

	@Override
	public void codelistSelected(UICodelist codelist) {
		Log.trace("Codelist selected "+codelist);
		if (selectedCodelist!=null && selectedCodelist.equals(codelist)) return;
		
		this.selectedCodelist = codelist;
	}

	@Override
	public void codelistDetails(UICodelist codelist) {
		Log.trace("codelistDetails "+codelist);
		retrieveMetadataTask.setSelectedCodelist(codelist);
		detailsNodeSelector.switchToCodeListDetails();
		publishEventBus.fireEvent(NavigationEvent.FORWARD);
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		selectedCodelist = null;
		view.reset();
	}
}
