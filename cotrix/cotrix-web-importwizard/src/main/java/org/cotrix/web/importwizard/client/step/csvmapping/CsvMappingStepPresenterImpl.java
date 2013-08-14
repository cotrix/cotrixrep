package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.TrackerLabels;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;
import org.cotrix.web.importwizard.shared.AttributesMappings;
import org.cotrix.web.importwizard.shared.ImportMetadata;
import org.cotrix.web.importwizard.shared.MappingMode;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class CsvMappingStepPresenterImpl extends AbstractWizardStep implements CsvMappingStepPresenter, MappingsUpdatedHandler, MetadataUpdatedHandler {

	protected CsvMappingStepView view;
	protected EventBus importEventBus;
	protected ImportMetadata metadata;
	protected AttributesMappings attributesMappings;
	
	@Inject
	public CsvMappingStepPresenterImpl(CsvMappingStepView view, @ImportBus EventBus importEventBus){
		super("csv-mapping", TrackerLabels.CUSTOMIZE, "Customize it", "Tell us what to import and how.", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		view.setPresenter(this);
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MappingsUpdatedEvent.TYPE, this);
		importEventBus.addHandler(MetadataUpdatedEvent.TYPE, this);
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
		
		String csvName = view.getCsvName();
		MappingMode mappingMode = view.getMappingMode();
		valid &= validateAttributes(csvName, mappingMode);
		
		if (valid) {
			
			attributesMappings = new AttributesMappings(mappings, mappingMode); 
			importEventBus.fireEvent(new MappingsUpdatedEvent(attributesMappings, true));
			
			if (metadata == null) metadata = new ImportMetadata();
			metadata.setName(csvName);
			importEventBus.fireEvent(new MetadataUpdatedEvent(metadata, true));
		}
		
		return valid;
	}
	
	protected boolean validateAttributes(String csvName, MappingMode mappingMode)
	{
		if (csvName==null || csvName.isEmpty()) {
			view.alert("You should choose a codelist name");
			return false;
		}
		
		return mappingMode!=null;
	}
	
	protected boolean validateMappings(List<AttributeMapping> mappings)
	{
		
		//only one code
		int codeCount = 0;
		for (AttributeMapping mapping:mappings) {
			Log.trace("mapping: "+mapping);
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
	public void onMappingUpdated(MappingsUpdatedEvent event) {
		if (event.isUserEdit()) return;
		attributesMappings = event.getMappings();
		view.setMapping(attributesMappings.getMappings());
		view.setMappingMode(attributesMappings.getMappingMode());
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) {
			view.setCsvName(event.getMetadata().getName());
			this.metadata = event.getMetadata();
		}
	}

	@Override
	public void onReload() {
		view.setCsvName(metadata.getName());
		view.setMapping(attributesMappings.getMappings());
		view.setMappingMode(attributesMappings.getMappingMode());
	}
}
