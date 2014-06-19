package org.cotrix.web.publish.client.wizard.step.typeselection;

import org.cotrix.web.common.shared.Format;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.wizard.client.event.NavigationEvent;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Source selection step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class TypeSelectionStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, TypeSelectionStepView.Presenter {
	
	protected static final ItemUpdatedEvent<Format> SDMX_EVENT = new ItemUpdatedEvent<Format>(Format.SDMX);
	protected static final ItemUpdatedEvent<Format> CSV_EVENT = new ItemUpdatedEvent<Format>(Format.CSV);
	protected static final ItemUpdatedEvent<Format> COMET_EVENT = new ItemUpdatedEvent<Format>(Format.COMET);

	protected TypeSelectionStepView view;
	
	@Inject
	@PublishBus
	protected EventBus importEventBus;

	@Inject
	public TypeSelectionStepPresenter(TypeSelectionStepView view) {
		super("typeSelection", TrackerLabels.TYPE, "How do we publish it?", "Choose a target format.", PublishWizardStepButtons.BACKWARD);
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
		importEventBus.fireEvent(SDMX_EVENT);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onCSVButtonClick() {
		Log.trace("onCSVButtonClick");
		importEventBus.fireEvent(CSV_EVENT);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}

	@Override
	public void onCometButtonClick() {
		Log.trace("onCometButtonClick");
		importEventBus.fireEvent(COMET_EVENT);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}
}
