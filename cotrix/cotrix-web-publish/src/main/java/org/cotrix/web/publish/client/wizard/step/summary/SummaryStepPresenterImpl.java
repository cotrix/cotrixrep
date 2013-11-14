package org.cotrix.web.publish.client.wizard.step.summary;

import org.cotrix.web.publish.client.event.CodeListSelectedEvent;
import org.cotrix.web.publish.client.event.MappingModeUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.MetadataUpdatedEvent;
import org.cotrix.web.publish.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.publish.shared.PublishMetadata;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;
import org.cotrix.web.share.shared.codelist.UICodelist;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SummaryStepPresenterImpl extends AbstractVisualWizardStep implements SummaryStepPresenter, MetadataUpdatedHandler{

	protected SummaryStepView view;
	protected EventBus publishBus;

	@Inject
	public SummaryStepPresenterImpl(SummaryStepView view, @PublishBus EventBus publishBus) {
		super("summary", TrackerLabels.SUMMARY, "Recap", "Here's the plan of action, let's do it.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.PUBLISH);
		this.view = view;
		
		this.publishBus = publishBus;
		publishBus.addHandler(MetadataUpdatedEvent.TYPE, this);
		bind();
	}
	
	protected void bind() {
		publishBus.addHandler(CodeListSelectedEvent.TYPE, new CodeListSelectedEvent.CodeListSelectedHandler() {
			
			@Override
			public void onCodeListSelected(CodeListSelectedEvent event) {
				UICodelist codelist = event.getSelectedCodelist();
				view.setCodelistName(codelist.getName());
				view.setCodelistVersion(codelist.getVersion());
				view.setState(codelist.getState());
			}
		});
		publishBus.addHandler(MappingsUpdatedEvent.TYPE, new MappingsUpdatedEvent.MappingsUpdatedHandler() {
			
			@Override
			public void onMappingUpdated(MappingsUpdatedEvent event) {
				view.setMapping(event.getMappings());
			}
		});
		
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				resetWizard();
			}
		});		
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
		publishBus.fireEvent(new MappingModeUpdatedEvent(mappingMode));
		return true;
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		
		PublishMetadata metadata = event.getMetadata();
		if (metadata.getOriginalName()==null || metadata.getOriginalName().equals(metadata.getName())) view.setCodelistName(metadata.getName());
		else view.setCodelistName(metadata.getOriginalName()+" as "+metadata.getName());
		
		view.setCodelistVersion(metadata.getVersion());
		
		this.view.setMetadataAttributes(metadata.getAttributes());		
	}

	protected void resetWizard() {
		view.setMappingMode(MappingMode.STRICT);
	}
}
