package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingLoadedEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadingEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingLoadedEvent.MappingLoadedHandler;
import org.cotrix.web.importwizard.client.event.MappingLoadingEvent.MappingLoadingHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractVisualWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.cotrix.web.importwizard.shared.ImportMetadata;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvMappingStepPresenterImpl extends AbstractVisualWizardStep implements CsvMappingStepPresenter, MetadataUpdatedHandler, MappingLoadingHandler, MappingLoadedHandler {

	protected CsvMappingStepView view;
	protected EventBus importEventBus;
	protected ImportMetadata metadata;
	protected List<AttributeMapping> mappings;
	
	@Inject
	public CsvMappingStepPresenterImpl(CsvMappingStepView view, @ImportBus EventBus importEventBus){
		super("csv-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tell us what to import and how.", NavigationButtonConfiguration.BACKWARD, NavigationButtonConfiguration.FORWARD);
		this.view = view;
		view.setPresenter(this);
		
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
		
		String csvName = view.getCsvName();
		String version = view.getVersion();
		valid &= validateAttributes(csvName, version);
		
		if (valid) {
			importEventBus.fireEvent(new MappingsUpdatedEvent(mappings));
			
			ImportMetadata metadata = new ImportMetadata();
			metadata.setName(csvName);
			metadata.setVersion(version);
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
		
		//only one code
		int codeCount = 0;
		for (AttributeMapping mapping:mappings) {
			//Log.trace("checking mapping: "+mapping);
			if (mapping.isMapped() && mapping.getAttributeDefinition().getType()==AttributeType.CODE) codeCount++;
			if (mapping.isMapped() && mapping.getAttributeDefinition().getName().isEmpty()) {
				view.alert("don't leave columns blank, bin them instead");
				return false;
			}
		}
		
		if (codeCount==0) {
			view.alert("One column must contains codes.");
			return false;
		}
		
		if (codeCount>1) {
			view.alert("Only one column can contains codes.");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onMappingLoading(MappingLoadingEvent event) {
		view.setMappingLoading();
	}
	
	@Override
	public void onMappingLoaded(MappingLoadedEvent event) {
		mappings = event.getMappings();
		view.setMapping(mappings);
		view.unsetMappingLoading();
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) {
			this.metadata = event.getMetadata();
			view.setCsvName(metadata.getName());
			view.setVersion(metadata.getVersion());
		}
	}

	@Override
	public void onReload() {
		view.setCsvName(metadata.getName());
		view.setVersion(metadata.getVersion());
		view.setMapping(mappings);
	}
}
