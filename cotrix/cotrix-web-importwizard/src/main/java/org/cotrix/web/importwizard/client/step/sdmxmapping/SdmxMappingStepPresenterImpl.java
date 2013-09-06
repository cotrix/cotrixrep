package org.cotrix.web.importwizard.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingLoadedEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadingEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadedEvent.MappingLoadedHandler;
import org.cotrix.web.importwizard.client.event.MappingLoadingEvent.MappingLoadingHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.cotrix.web.importwizard.shared.ImportMetadata;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxMappingStepPresenterImpl extends AbstractWizardStep implements SdmxMappingStepPresenter, MetadataUpdatedHandler, MappingLoadingHandler, MappingLoadedHandler  {

	protected SdmxMappingStepView view;
	protected EventBus importEventBus;
	protected ImportMetadata metadata;
	protected AttributesMappings attributesMappings;

	@Inject
	public SdmxMappingStepPresenterImpl(SdmxMappingStepView view, @ImportBus EventBus importEventBus){
		super("sdmx-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tell us what to import and how.", NavigationButtonConfiguration.BACKWARD, NavigationButtonConfiguration.FORWARD);
		this.view = view;
		this.view.setPresenter(this);

		this.importEventBus = importEventBus;
		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, this);
		importEventBus.addHandler(MappingLoadingEvent.TYPE, this);
		importEventBus.addHandler(MappingLoadedEvent.TYPE, this);
	}

	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}

	public boolean isComplete() {
		Log.trace("checking csv mapping");

		List<AttributeMapping> mappings = view.getMappings();
		Log.trace(mappings.size()+" mappings to check");

		boolean valid = validateMappings(mappings);
		String codelistName = view.getCodelistName();
		valid &= validateAttributes(codelistName);

		if (valid) {

			attributesMappings = new AttributesMappings(mappings, null); 
			importEventBus.fireEvent(new MappingsUpdatedEvent(attributesMappings));
			
			if (metadata == null) metadata = new ImportMetadata();
			metadata.setName(codelistName);
			importEventBus.fireEvent(new MetadataUpdatedEvent(metadata, true));
		}

		return valid;
	}
	

	protected boolean validateAttributes(String codelistName)
	{
		if (codelistName==null || codelistName.isEmpty()) {
			view.alert("You should choose a codelist name");
			return false;
		}
		
		return true;
	}

	protected boolean validateMappings(List<AttributeMapping> mappings)
	{

		for (AttributeMapping mapping:mappings) {
			if (mapping.isMapped() && mapping.getAttributeDefinition().getName().isEmpty()) {
				view.alert("don't leave elements blank, bin them instead");
				return false;
			}
		}

		return true;
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) {
			String name = event.getMetadata().getName();
			view.setCodelistName(name == null?"":name);
			this.metadata = event.getMetadata();
		}
	}
	
	@Override
	public void onMappingLoading(MappingLoadingEvent event) {
		view.setMappingLoading();
	}
	
	@Override
	public void onMappingLoaded(MappingLoadedEvent event) {
		attributesMappings = event.getMappings();
		view.setMappings(attributesMappings.getMappings());
		view.unsetMappingLoading();
	}

	@Override
	public void onReload() {
		view.setCodelistName(metadata.getName());
		view.setMappings(attributesMappings.getMappings());
	}
}
