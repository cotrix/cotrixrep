package org.cotrix.web.publish.client.wizard.step.destinationselection;

import org.cotrix.web.publish.client.event.DestinationTypeChangeEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
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

	protected DestinationSelectionStepView view;
	
	@Inject
	@PublishBus
	protected EventBus importEventBus;

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
		importEventBus.fireEvent(DestinationTypeChangeEvent.CHANNEL);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onLocalButtonClick() {
		Log.trace("onLocalButtonClick");
		importEventBus.fireEvent(DestinationTypeChangeEvent.FILE);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}
}
