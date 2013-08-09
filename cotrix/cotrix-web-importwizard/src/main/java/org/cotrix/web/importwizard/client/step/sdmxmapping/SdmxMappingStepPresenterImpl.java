package org.cotrix.web.importwizard.client.step.sdmxmapping;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent;
import org.cotrix.web.importwizard.client.event.MappingsUpdatedEvent.MappingsUpdatedHandler;
import org.cotrix.web.importwizard.client.step.AbstractWizardStep;
import org.cotrix.web.importwizard.client.wizard.NavigationButtonConfiguration;
import org.cotrix.web.importwizard.shared.AttributeMapping;
import org.cotrix.web.importwizard.shared.AttributesMappings;

import com.allen_sauer.gwt.log.client.Log;
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
		super("sdmx-mapping", "Customize", "Customize it", NavigationButtonConfiguration.DEFAULT_BACKWARD, NavigationButtonConfiguration.DEFAULT_FORWARD);
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
		Log.trace("checking csv mapping");

		List<AttributeMapping> mappings = view.getMappings();
		Log.trace(mappings.size()+" mappings to check");

		boolean valid = validateMappings(mappings);

		if (valid) {

			AttributesMappings attributesMappings = new AttributesMappings(mappings, null); 
			importEventBus.fireEvent(new MappingsUpdatedEvent(attributesMappings, true));
		}

		return valid;
	}

	protected boolean validateMappings(List<AttributeMapping> mappings)
	{

		for (AttributeMapping mapping:mappings) {
			if (mapping.isMapped() && mapping.getAttributeDefinition().getName().isEmpty()) {
				view.alert("Name field required");
				return false;
			}
		}

		return true;
	}


	@Override
	public void onMappingUpdated(MappingsUpdatedEvent event) {
		if (event.isUserEdit()) return;
		AttributesMappings attributesMappings = event.getMappings();
		view.setAttributes(attributesMappings.getMappings());
	}
}
