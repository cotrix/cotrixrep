package org.cotrix.web.importwizard.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributeType;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SdmxMappingStepPresenterImpl extends AbstractWizardStep implements SdmxMappingStepPresenter, MappingsUpdatedHandler {

	protected SdmxMappingStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public SdmxMappingStepPresenterImpl(SdmxMappingStepView view, @ImportBus EventBus importEventBus){
		super("sdmx-mapping", "SDMX Mapping", "SDMX Mapping", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MappingsUpdatedEvent.TYPE, this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public boolean isComplete() {
		List<AttributeMapping> columns = view.getMappings();
		
		boolean valid = validate(columns);
		
		if (valid) importEventBus.fireEvent(new MappingsUpdatedEvent(columns, true));
		
		return valid;
	}
	
	protected boolean validate(List<AttributeMapping> mappings)
	{
		
		//only one code
		int codeCount = 0;
		for (AttributeMapping mapping:mappings) {
			if (mapping.isMapped() && mapping.getAttributeDefinition().getType()==AttributeType.CODE) codeCount++;
			if (mapping.isMapped() && mapping.getAttributeDefinition().getName().isEmpty()) {
				view.alert("Name field required");
				return false;
			}
		}
		
		if (codeCount==0) {
			view.alert("One code attribute required");
			return false;
		}
		
		if (codeCount>1) {
			view.alert("You can assign only one code attribute.");
			return false;
		}
		
		return true;
	}

	@Override
	public void onMappingUpdated(MappingsUpdatedEvent event) {
		if (event.isUserEdit()) return;
		view.setAttributes(event.getMappings());
	}
}
