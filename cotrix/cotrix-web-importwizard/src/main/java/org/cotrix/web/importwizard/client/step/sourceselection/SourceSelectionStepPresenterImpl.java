package org.cotrix.web.importwizard.client.step.sourceselection;

import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.SourceTypeChangeEvent;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.event.NavigationEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Source selection step.
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceSelectionStepPresenterImpl extends AbstractWizardStep implements SourceSelectionStepPresenter {

	protected SourceSelectionStepView view;
	
	@Inject
	@ImportBus
	protected EventBus importEventBus;

	@Inject
	public SourceSelectionStepPresenterImpl(SourceSelectionStepView view) {
		super("sourceSelection", TrackerLabels.LOCATE, "Where is the codelist?", "If you don't have it, we can find it.", NavigationButtonConfiguration.NONE, NavigationButtonConfiguration.NONE);
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
	public boolean isComplete() {
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
