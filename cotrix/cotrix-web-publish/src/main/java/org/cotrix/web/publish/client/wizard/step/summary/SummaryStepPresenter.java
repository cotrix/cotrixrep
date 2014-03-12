package org.cotrix.web.publish.client.wizard.step.summary;

import org.cotrix.web.common.shared.codelist.UICodelist;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.MappingMode;
import org.cotrix.web.publish.shared.PublishMetadata;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.wizard.client.step.VisualWizardStep;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SummaryStepPresenter extends AbstractVisualWizardStep implements VisualWizardStep {

	protected SummaryStepView view;
	protected EventBus publishBus;

	@Inject
	public SummaryStepPresenter(SummaryStepView view, @PublishBus EventBus publishBus) {
		super("summary", TrackerLabels.SUMMARY, "Recap", "Here's the plan of action, let's do it.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.PUBLISH);
		this.view = view;
		
		this.publishBus = publishBus;

		bind();
	}
	
	protected void bind() {
		publishBus.addHandler(ItemSelectedEvent.getType(UICodelist.class), new ItemSelectedEvent.ItemSelectedHandler<UICodelist>() {

			@Override
			public void onItemSelected(ItemSelectedEvent<UICodelist> event) {
				UICodelist codelist = event.getItem();
				view.setCodelistName(codelist.getName());
				view.setCodelistVersion(codelist.getVersion());
				view.setState(codelist.getState().toString());
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
		
		publishBus.addHandler(ItemUpdatedEvent.getType(PublishMetadata.class), new ItemUpdatedEvent.ItemUpdatedHandler<PublishMetadata>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<PublishMetadata> event) {
				setMetadata(event.getItem());
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
		publishBus.fireEvent(new ItemUpdatedEvent<MappingMode>(mappingMode));
		return true;
	}


	protected void setMetadata(PublishMetadata metadata) {
		
		if (metadata.getOriginalName()==null || metadata.getOriginalName().equals(metadata.getName())) view.setCodelistName(metadata.getName());
		else view.setCodelistName(metadata.getOriginalName()+" as "+metadata.getName());
		
		view.setCodelistVersion(metadata.getVersion());
		
		this.view.setMetadataAttributes(metadata.getAttributes());		
	}

	protected void resetWizard() {
		view.setMappingMode(MappingMode.STRICT);
	}
}
