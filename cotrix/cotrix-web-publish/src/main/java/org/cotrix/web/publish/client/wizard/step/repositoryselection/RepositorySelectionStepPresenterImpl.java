package org.cotrix.web.publish.client.wizard.step.repositoryselection;

import org.cotrix.web.publish.client.event.ItemDetailsRequestedEvent;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.FormatType;
import org.cotrix.web.publish.shared.UIRepository;
import org.cotrix.web.share.client.wizard.event.NavigationEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositorySelectionStepPresenterImpl extends AbstractVisualWizardStep implements RepositorySelectionStepPresenter {

	protected final RepositorySelectionStepView view;
	
	protected EventBus publishBus;
	
	protected UIRepository selectedRepository;
	
	protected boolean repositoryDetails = false;
	
	@Inject
	public RepositorySelectionStepPresenterImpl(RepositorySelectionStepView view, @PublishBus EventBus publishBus) {
		super("repositorySelection", TrackerLabels.TARGET, "Pick your target", "We found a few repositories nearby.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.FORWARD);
		this.view = view;
		this.view.setPresenter(this);
		this.publishBus = publishBus;
		
		bind();
	}
	
	protected void bind() {
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}
		});
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		Log.trace("SelectionStep leaving");
		return selectedRepository!=null || repositoryDetails;
	}

	@Override
	public void repositorySelected(UIRepository repository) {
		Log.trace("Codelist selected "+repository);
		if (selectedRepository!=null && selectedRepository.equals(repository)) return;
		
		this.selectedRepository = repository;
		publishBus.fireEvent(new ItemSelectedEvent<UIRepository>(repository));
		publishBus.fireEvent(new ItemUpdatedEvent<FormatType>(repository.getPublishedType()));
	}

	@Override
	public void repositoryDetails(UIRepository repository) {
		
		publishBus.fireEvent(new ItemDetailsRequestedEvent<UIRepository>(repository));
		repositoryDetails = true;
		publishBus.fireEvent(NavigationEvent.FORWARD);
		repositoryDetails = false;
	}


	protected void reset() {
		view.reset();
	}

}
