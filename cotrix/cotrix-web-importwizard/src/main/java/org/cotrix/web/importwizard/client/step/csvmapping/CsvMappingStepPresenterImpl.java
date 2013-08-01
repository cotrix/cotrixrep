package org.cotrix.web.importwizard.client.step.csvmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MetadataUpdatedEvent.MetadataUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;

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
	
	@Inject
	public CsvMappingStepPresenterImpl(CsvMappingStepView view, @ImportBus EventBus importEventBus){
		super("csv-mapping", "Columns mapping", "Columns mapping", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		
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
		
		boolean valid = validate(mappings);
		
		if (valid) importEventBus.fireEvent(new MappingsUpdatedEvent(mappings, true));
		
		return valid;
	}
	
	protected boolean validate(List<AttributeMapping> mappings)
	{
		
		//only one code
		int codeCount = 0;
		for (AttributeMapping mapping:mappings) {
			Log.trace("mapping: "+mapping);
			if (mapping.isMapped() && mapping.getAttributeDefinition().getType()==AttributeType.CODE) codeCount++;
			if (mapping.isMapped() && mapping.getAttributeDefinition().getName().isEmpty()) {
				view.alert("Name field required");
				return false;
			}
		}
		
		if (codeCount==0) {
			view.alert("One mapping to code required");
			return false;
		}
		
		if (codeCount>1) {
			view.alert("You can assign only one code.");
			return false;
		}
		
		return true;
	}

	@Override
	public void onMappingUpdated(MappingsUpdatedEvent event) {
		if (event.isUserEdit()) return;
		view.setMapping(event.getMappings());
	}

	@Override
	public void onMetadataUpdated(MetadataUpdatedEvent event) {
		if (!event.isUserEdited()) view.setCsvName(event.getMetadata().getName());
	}
}
