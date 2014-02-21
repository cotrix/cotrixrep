package org.cotrix.web.ingest.client.step.sourceselection;

import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.SourceTypeChangeEvent;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.wizard.client.event.NavigationEvent;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Source selection step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceSelectionStepPresenterImpl extends AbstractVisualWizardStep implements SourceSelectionStepPresenter {

	protected SourceSelectionStepView view;
	
	@Inject
	@ImportBus
	protected EventBus importEventBus;

	@Inject
	public SourceSelectionStepPresenterImpl(SourceSelectionStepView view) {
		super("sourceSelection", TrackerLabels.LOCATE, "Where is the codelist?", "Either you have it or we try to find it.");
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
		importEventBus.fireEvent(SourceTypeChangeEvent.CHANNEL);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onLocalButtonClick() {
		Log.trace("onLocalButtonClick");
		importEventBus.fireEvent(SourceTypeChangeEvent.FILE);
		importEventBus.fireEvent(NavigationEvent.FORWARD);
	}
}
