package org.cotrix.web.importwizard.client.step.summary;

import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.ImportStartedEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.CodeListSelectedEvent.CodeListSelectedHandler;
import org.cotrix.web.importwizard.client.event.FileUploadedEvent.FileUploadedHandler;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.event.ImportStartedEvent.ImportStartedHandler;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.resources.Resources;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration.ButtonAction;
import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SummaryStepPresenterImpl extends AbstractWizardStep implements SummaryStepPresenter, MetadataUpdatedHandler, MappingsUpdatedHandler, 
ImportStartedHandler, ImportProgressHandler, FileUploadedHandler, CodeListSelectedHandler {

	protected SummaryStepView view;
	protected EventBus importEventBus;

	@Inject
	public SummaryStepPresenterImpl(SummaryStepView view, @ImportBus EventBus importEventBus) {
		super("summary", TrackerLabels.SUMMARY, "Recap", "Here's the plan of action, let's do it.", NavigationButtonConfiguration.DEFAULT_BACKWARD, new NavigationButtonConfiguration("Import", ButtonAction.NEXT, Resources.INSTANCE.css().blueButton()));
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, this);
		importEventBus.addHandler(MappingsUpdatedEvent.TYPE, this);
		importEventBus.addHandler(ImportStartedEvent.TYPE, this);
		importEventBus.addHandler(ImportProgressEvent.TYPE, this);
		importEventBus.addHandler(FileUploadedEvent.TYPE, this);
		importEventBus.addHandler(CodeListSelectedEvent.TYPE, this);
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
		AttributesMappings attributesMappings = event.getMappings();
		this.view.setMapping(attributesMappings.getMappings());
		
		if (attributesMappings.getMappingMode()==null) view.setMappingModeVisible(false);
		else {
			String mode = attributesMappings.getMappingMode()==MappingMode.STRICT?"No":"Yes";
			view.setMappingMode(mode);
			view.setMappingModeVisible(true);
		}
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		
		ImportMetadata metadata = event.getMetadata();
		if (metadata.getOriginalName()==null || metadata.getOriginalName().equals(metadata.getName())) view.setCodelistName(metadata.getName());
		else view.setCodelistName(metadata.getOriginalName()+" as "+metadata.getName());
		
		this.view.setMetadataAttributes(metadata.getAttributes());		
	}

	@Override
	public void onImportStarted(ImportStartedEvent event) {
		view.showProgress();		
	}

	@Override
	public void onImportProgress(ImportProgressEvent event) {
		if (event.getProgress().isComplete()) view.hideProgress();
	}

	@Override
	public void onFileUploaded(FileUploadedEvent event) {
		view.setFileName(event.getFileName());
		view.setFileNameVisible(true);
	}

	@Override
	public void onCodeListSelected(CodeListSelectedEvent event) {
		view.setFileNameVisible(false);
	}
}
