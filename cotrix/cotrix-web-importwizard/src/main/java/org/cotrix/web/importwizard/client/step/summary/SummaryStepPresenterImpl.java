package org.cotrix.web.importwizard.client.step.summary;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingUpdatedEvent.MappingUpdatedHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SummaryStepPresenterImpl extends AbstractWizardStep implements SummaryStepPresenter, MetadataUpdatedHandler, MappingUpdatedHandler {

	protected SummaryStepView view;
	protected EventBus importEventBus;

	@Inject
	public SummaryStepPresenterImpl(SummaryStepView view, @ImportBus EventBus importEventBus) {
		super("summary", "Summary", "Summary", NavigationButtonConfiguration.DEFAULT_BACKWARD, new NavigationButtonConfiguration("Save"));
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, this);
		importEventBus.addHandler(MappingUpdatedEvent.TYPE, this);
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
	public void onMappingUpdated(MappingUpdatedEvent event) {
		this.view.setColumns(event.getColumns());		
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		this.view.setMetadata(event.getMetadata());
		
	}
}
