package org.cotrix.web.publish.client.wizard.step.repositorydetails;

import org.cotrix.web.common.shared.codelist.RepositoryDetails;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryDetailsStepPresenterImpl extends AbstractVisualWizardStep implements RepositoryDetailsStepPresenter {

	protected final RepositoryDetailsStepView view;
	
	protected EventBus publishBus;
	
	@Inject
	public RepositoryDetailsStepPresenterImpl(RepositoryDetailsStepView view, @PublishBus EventBus publishBus) {
		super("repositoryDetails", TrackerLabels.TARGET, "Repository Details", "", PublishWizardStepButtons.BACKWARD);
		this.view = view;
		this.publishBus = publishBus;
		bind();
	}
	
	protected void bind() {
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				
			}
		});
		
		publishBus.addHandler(ItemUpdatedEvent.getType(RepositoryDetails.class), new ItemUpdatedEvent.ItemUpdatedHandler<RepositoryDetails>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<RepositoryDetails> event) {
				Log.trace("getting RepositoryDetails for "+event.getItem());
				view.setRepository(event.getItem());
			}
			
		});
		
	}

	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean leave() {
		return false;
	}
	
	protected void setRepository(RepositoryDetails repository) {
		view.setRepository(repository);
		configuration.setTitle(repository.getName()+" details");
	}
}
