package org.cotrix.web.importwizard.client.step.summary;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportStartedEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.event.ImportStartedEvent.ImportStartedHandler;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SummaryStepPresenterImpl extends AbstractWizardStep implements SummaryStepPresenter, MetadataUpdatedHandler, MappingsUpdatedHandler, ImportStartedHandler, ImportProgressHandler {

	protected SummaryStepView view;
	protected EventBus importEventBus;

	@Inject
	public SummaryStepPresenterImpl(SummaryStepView view, @ImportBus EventBus importEventBus) {
		super("summary", "Summary", "Quick recap", NavigationButtonConfiguration.DEFAULT_BACKWARD, new NavigationButtonConfiguration("Save"));
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, this);
		importEventBus.addHandler(MappingsUpdatedEvent.TYPE, this);
		importEventBus.addHandler(ImportStartedEvent.TYPE, this);
		importEventBus.addHandler(ImportProgressEvent.TYPE, this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	public void onMappingUpdated(MappingsUpdatedEvent event) {
		this.view.setMapping(event.getMappings());		
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		this.view.setMetadata(event.getMetadata());
		
	}

	@Override
	public void onImportStarted(ImportStartedEvent event) {
		view.showProgress();		
	}

	@Override
	public void onImportProgress(ImportProgressEvent event) {
		if (event.getProgress().isComplete()) view.hideProgress();
	}
}
