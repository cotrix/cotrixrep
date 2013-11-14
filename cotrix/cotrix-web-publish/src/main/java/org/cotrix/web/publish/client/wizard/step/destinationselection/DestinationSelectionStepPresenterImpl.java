package org.cotrix.web.publish.client.wizard.step.destinationselection;

import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.DestinationType;
import org.cotrix.web.share.client.wizard.event.NavigationEvent;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Source selection step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DestinationSelectionStepPresenterImpl extends AbstractVisualWizardStep implements DestinationSelectionStepPresenter {

	protected static final ItemUpdatedEvent<DestinationType> CHANNEL_EVENT = new ItemUpdatedEvent<DestinationType>(DestinationType.CHANNEL);
	protected static final ItemUpdatedEvent<DestinationType> FILE_EVENT = new ItemUpdatedEvent<DestinationType>(DestinationType.FILE);
	
	protected DestinationSelectionStepView view;
	
	@Inject
	@PublishBus
	protected EventBus publishBus;

	@Inject
	public DestinationSelectionStepPresenterImpl(DestinationSelectionStepView view) {
		super("destinationSelection", TrackerLabels.DESTINATION, "Where is it going?", "Save it to your computer, or send it away to the cloud.", PublishWizardStepButtons.BACKWARD);
		this.view = view;
		this.view.setPresenter(this);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	/** 
	 * {@inheritDoc}
	 */
	public boolean leave() {
		return true;
	}

	public void alert(String message) {
		view.alert(message);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onCloudButtonClick() {
		Log.trace("onCloudButtonClick");
		publishBus.fireEvent(CHANNEL_EVENT);
		publishBus.fireEvent(NavigationEvent.FORWARD);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onLocalButtonClick() {
		Log.trace("onLocalButtonClick");
		publishBus.fireEvent(FILE_EVENT);
		publishBus.fireEvent(NavigationEvent.FORWARD);
	}
}
