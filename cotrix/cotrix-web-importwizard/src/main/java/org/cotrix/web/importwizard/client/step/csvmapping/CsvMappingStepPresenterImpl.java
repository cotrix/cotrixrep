package org.cotrix.web.importwizard.client.step.csvmapping;

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
public class CsvMappingStepPresenterImpl extends AbstractWizardStep implements CsvMappingStepPresenter, MappingUpdatedHandler {

	protected CsvMappingStepView view;
	protected EventBus importEventBus;
	
	@Inject
	public CsvMappingStepPresenterImpl(CsvMappingStepView view, @ImportBus EventBus importEventBus){
		super("csv-mapping", "Columns mapping", "Columns mapping", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
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
		List<AttributeMapping> mapping = view.getMapping();
		
		boolean valid = validate(mapping);
		
		if (valid) importEventBus.fireEvent(new MappingUpdatedEvent(mapping, true));
		
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
	public void onMappingUpdated(MappingUpdatedEvent event) {
		if (event.isUserEdit()) return;
		view.setMapping(event.getMapping());
	}
}
