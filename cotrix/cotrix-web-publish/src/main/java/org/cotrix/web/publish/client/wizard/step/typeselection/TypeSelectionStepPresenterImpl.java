package org.cotrix.web.publish.client.wizard.step.typeselection;

import org.cotrix.web.publish.client.event.FormatTypeChangeEvent;
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
public class TypeSelectionStepPresenterImpl extends AbstractVisualWizardStep implements TypeSelectionStepPresenter {

	protected TypeSelectionStepView view;
	
	@Inject
	@PublishBus
	protected EventBus importEventBus;

	@Inject
	public TypeSelectionStepPresenterImpl(TypeSelectionStepView view) {
		super("typeSelection", TrackerLabels.TYPE, "Which format to publish the codelist?", "Either you have it or we try to find it.", PublishWizardStepButtons.BACKWARD);
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
	public void onSDMXButtonClick() {
		Log.trace("onSDMXButtonClick");
		importEventBus.fireEvent(FormatTypeChangeEvent.SDMX);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onCSVButtonClick() {
		Log.trace("onCSVButtonClick");
		importEventBus.fireEvent(FormatTypeChangeEvent.CSV);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}
}
