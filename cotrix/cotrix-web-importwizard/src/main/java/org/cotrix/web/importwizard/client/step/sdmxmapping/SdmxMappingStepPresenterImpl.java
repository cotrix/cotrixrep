package org.cotrix.web.importwizard.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingUpdatedEvent.MappingUpdatedHandler;
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
public class SdmxMappingStepPresenterImpl extends AbstractWizardStep implements SdmxMappingStepPresenter, MappingUpdatedHandler {

	protected SdmxMappingStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public SdmxMappingStepPresenterImpl(SdmxMappingStepView view, @ImportBus EventBus importEventBus){
		super("sdmx-mapping", "Define Type", "Define Type", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
		this.view = view;
		
		this.importEventBus = importEventBus;
		importEventBus.addHandler(MappingUpdatedEvent.TYPE, this);
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
	
	public boolean isComplete() {
		List<AttributeMapping> columns = view.getAttributes();
		
		boolean valid = validate(columns);
		
		if (valid) importEventBus.fireEvent(new MappingUpdatedEvent(columns, true));
		
		return valid;
	}
	
	protected boolean validate(List<AttributeMapping> mapping)
	{
		
		//only one code
		int codeCount = 0;
		for (AttributeMapping attributeMapping:mapping) {
			if (attributeMapping.isMapped() && attributeMapping.getAttributeDefinition().getType()==AttributeType.CODE) codeCount++;
		
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
	public void onMappingUpdated(MappingUpdatedEvent event) {
		if (event.isUserEdit()) return;
		view.setAttributes(event.getMapping());
	}
}
