package org.cotrix.web.publish.client.wizard.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.publish.client.event.MappingLoadedEvent;
import org.cotrix.web.publish.client.event.MappingLoadedEvent.MappingLoadedHandler;
import org.cotrix.web.publish.client.event.MappingLoadingEvent;
import org.cotrix.web.publish.client.event.MappingLoadingEvent.MappingLoadingHandler;
import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.MetadataUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.publish.client.wizard.PublishWizardStepButtons;
import org.cotrix.web.publish.client.wizard.step.TrackerLabels;
import org.cotrix.web.publish.shared.AttributeMapping;
import org.cotrix.web.publish.shared.ImportMetadata;
import org.cotrix.web.share.client.wizard.step.AbstractVisualWizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxMappingStepPresenterImpl extends AbstractVisualWizardStep implements SdmxMappingStepPresenter, MetadataUpdatedHandler, MappingLoadingHandler, MappingLoadedHandler  {

	protected SdmxMappingStepView view;
	protected EventBus importEventBus;
	protected ImportMetadata metadata;
	protected List<AttributeMapping> mappings;

	@Inject
	public SdmxMappingStepPresenterImpl(SdmxMappingStepView view, @PublishBus EventBus importEventBus){
		super("sdmx-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tell us what to import and how.", PublishWizardStepButtons.BACKWARD, PublishWizardStepButtons.FORWARD);
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

	public boolean leave() {
		Log.trace("checking csv mapping");

		List<AttributeMapping> mappings = view.getMappings();
		Log.trace(mappings.size()+" mappings to check");

		boolean valid = validateMappings(mappings);
		String codelistName = view.getCodelistName();
		String version = view.getVersion();
		boolean sealed = view.getSealed();
		valid &= validateAttributes(codelistName, version);

		if (valid) {

			importEventBus.fireEvent(new MappingsUpdatedEvent(mappings));
			
			ImportMetadata metadata = new ImportMetadata();
			metadata.setName(codelistName);
			metadata.setVersion(version);
			metadata.setSealed(sealed);
			metadata.setAttributes(this.metadata.getAttributes());
			importEventBus.fireEvent(new MetadataUpdatedEvent(metadata, true));
		}

		return valid;
	}
	

	protected boolean validateAttributes(String csvName, String version)
	{
		if (csvName==null || csvName.isEmpty()) {
			view.alert("You should choose a codelist name");
			return false;
		}
		
		if (version==null || version.isEmpty()) {
			view.alert("You should specify a codelist version");
			return false;
		}
		
		return true;
	}

	protected boolean validateMappings(List<AttributeMapping> mappings)
	{

		for (AttributeMapping mapping:mappings) {
			if (mapping.isMapped() && mapping.getAttributeDefinition().getName().getLocalPart().isEmpty()) {
				view.alert("don't leave elements blank, bin them instead");
				return false;
			}
		}

		return true;
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) {
			this.metadata = event.getMetadata();
			String name = metadata.getName();
			view.setCodelistName(name == null?"":name);
			view.setVersion(metadata.getVersion());
			view.setSealed(metadata.isSealed());
		}
	}
	
	@Override
	public void onMappingLoading(MappingLoadingEvent event) {
		view.setMappingLoading();
	}
	
	@Override
	public void onMappingLoaded(MappingLoadedEvent event) {
		mappings = event.getMappings();
		view.setMappings(mappings);
		view.unsetMappingLoading();
	}

	@Override
	public void onReload() {
		view.setCodelistName(metadata.getName());
		view.setVersion(metadata.getVersion());
		view.setSealed(metadata.isSealed());
		view.setMappings(mappings);
	}
}
