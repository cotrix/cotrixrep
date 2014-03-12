package org.cotrix.web.publish.client.wizard.step.destinationselection;

import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.Destination;
import org.cotrix.web.wizard.client.event.NavigationEvent;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Source selection step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DestinationSelectionStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, DestinationSelectionStepView.Presenter {

	protected static final ItemUpdatedEvent<Destination> CHANNEL_EVENT = new ItemUpdatedEvent<Destination>(Destination.CHANNEL);
	protected static final ItemUpdatedEvent<Destination> FILE_EVENT = new ItemUpdatedEvent<Destination>(Destination.FILE);
	
	protected DestinationSelectionStepView view;
	
	@Inject
	@PublishBus
	protected EventBus publishBus;

	@Inject
	public DestinationSelectionStepPresenter(DestinationSelectionStepView view) {
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
