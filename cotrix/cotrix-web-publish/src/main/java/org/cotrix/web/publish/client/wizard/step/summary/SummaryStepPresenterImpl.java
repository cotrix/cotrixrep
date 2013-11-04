package org.cotrix.web.publish.client.wizard.step.summary;

import java.util.List;

import org.cotrix.web.publish.client.event.MappingLoadedEvent;
import org.cotrix.web.publish.client.event.MappingModeUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.MetadataUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.event.MappingLoadedEvent.MappingLoadedHandler;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.publish.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.ImportMetadata;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SummaryStepPresenterImpl extends AbstractVisualWizardStep implements SummaryStepPresenter, MetadataUpdatedHandler, MappingLoadedHandler, MappingsUpdatedHandler, ResetWizardHandler {

	protected SummaryStepView view;
	protected EventBus importEventBus;

	@Inject
	public SummaryStepPresenterImpl(SummaryStepView view, @PublishBus EventBus importEventBus) {
		super("summary", TrackerLabels.SUMMARY, "Recap", "Here's the plan of action, let's do it.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.PUBLISH);
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, this);
		importEventBus.addHandler(MappingsUpdatedEvent.TYPE, this);
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}
	
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public boolean leave() {
		MappingMode mappingMode = view.getMappingMode();
		importEventBus.fireEvent(new MappingModeUpdatedEvent(mappingMode));
		return true;
	}
	
	@Override
	public void onMappingLoaded(MappingLoadedEvent event) {
		List<AttributeMapping> attributesMappings = event.getMappings();
		view.setMapping(attributesMappings);
	}

	@Override
	public void onMappingUpdated(MappingsUpdatedEvent event) {
		List<AttributeMapping> attributesMappings = event.getMappings();
		view.setMapping(attributesMappings);
		
		/*
		if (attributesMappings.getMappingMode()==null) view.setMappingModeVisible(false);
		else {
			view.setMappingMode(attributesMappings.getMappingMode());
			view.setMappingModeVisible(true);
		}*/
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		
		ImportMetadata metadata = event.getMetadata();
		if (metadata.getOriginalName()==null || metadata.getOriginalName().equals(metadata.getName())) view.setCodelistName(metadata.getName());
		else view.setCodelistName(metadata.getOriginalName()+" as "+metadata.getName());
		
		view.setCodelistVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
		
		this.view.setMetadataAttributes(metadata.getAttributes());		
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		view.setMappingMode(MappingMode.STRICT);
	}


}
