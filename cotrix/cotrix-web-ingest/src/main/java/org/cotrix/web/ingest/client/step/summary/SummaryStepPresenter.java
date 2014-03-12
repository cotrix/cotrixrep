package org.cotrix.web.ingest.client.step.summary;

import java.util.List;

import org.cotrix.web.ingest.client.event.CodeListSelectedEvent;
import org.cotrix.web.ingest.client.event.FileUploadedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.MappingLoadedEvent;
import org.cotrix.web.ingest.client.event.MappingModeUpdatedEvent;
import org.cotrix.web.ingest.client.event.MappingsUpdatedEvent;
import org.cotrix.web.ingest.client.event.MetadataUpdatedEvent;
import org.cotrix.web.ingest.client.step.TrackerLabels;
import org.cotrix.web.ingest.client.wizard.ImportWizardStepButtons;
import org.cotrix.web.ingest.shared.AttributeMapping;
import org.cotrix.web.ingest.shared.ImportMetadata;
import org.cotrix.web.ingest.shared.MappingMode;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SummaryStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep, ResetWizardHandler {

	protected interface SummaryStepPresenterEventBinder extends EventBinder<SummaryStepPresenter> {}
	
	protected SummaryStepView view;
	protected EventBus importEventBus;

	@Inject
	public SummaryStepPresenter(SummaryStepView view, @ImportBus EventBus importEventBus) {
		super("summary", TrackerLabels.SUMMARY, "Recap", "Here's the plan of action, let's do it.", ImportWizardStepButtons.BACKWARD, ImportWizardStepButtons.IMPORT);
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
	}
	
	@Inject
	private void bind(SummaryStepPresenterEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
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
	
	@EventHandler
	void onMappingLoaded(MappingLoadedEvent event) {
		List<AttributeMapping> attributesMappings = event.getMappings();
		view.setMapping(attributesMappings);
	}

	@EventHandler
	void onMappingUpdated(MappingsUpdatedEvent event) {
		List<AttributeMapping> attributesMappings = event.getMappings();
		view.setMapping(attributesMappings);
		
		/*
		if (attributesMappings.getMappingMode()==null) view.setMappingModeVisible(false);
		else {
			view.setMappingMode(attributesMappings.getMappingMode());
			view.setMappingModeVisible(true);
		}*/
	}

	@EventHandler
	void onMetadataUpdated(MetadataUpdatedEvent event) {
		
		ImportMetadata metadata = event.getMetadata();
		if (metadata.getOriginalName()==null || metadata.getOriginalName().equals(metadata.getName())) view.setCodelistName(metadata.getName());
		else view.setCodelistName(metadata.getOriginalName()+" as "+metadata.getName());
		
		view.setCodelistVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
		
		this.view.setMetadataAttributes(metadata.getAttributes());		
	}

	@EventHandler
	void onFileUploaded(FileUploadedEvent event) {
		view.setFileName(event.getFileName());
		view.setFileNameVisible(true);
	}

	@EventHandler
	void onCodeListSelected(CodeListSelectedEvent event) {
		view.setFileNameVisible(false);
	}

	@Override
	public void onResetWizard(ResetWizardEvent event) {
		view.setMappingMode(MappingMode.STRICT);
	}


}
