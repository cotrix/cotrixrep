/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import java.util.List;

import org.cotrix.web.publish.client.event.CodeListType;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.publish.client.wizard.step.sdmxmapping.SdmxMappingStepPresenter;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.wizard.client.flow.FlowNode;
import org.cotrix.web.wizard.client.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class MappingNodeSelector extends AbstractNodeSelector<WizardStep> {
	
	protected WizardStep nextStep;
	protected WizardStep oldNextStep;
	
	protected CsvMappingStepPresenter csvStep;
	
	@Inject
	protected SdmxMappingStepPresenter sdmxStep;
	
	@Inject
	public MappingNodeSelector(@PublishBus EventBus publishBus, CsvMappingStepPresenter csvStep)
	{
		this.csvStep = csvStep;
		this.nextStep = csvStep;

		bind(publishBus);
	}
	
	protected void bind(EventBus publishBus) {
		publishBus.addHandler(ItemUpdatedEvent.getType(CodeListType.class), new ItemUpdatedEvent.ItemUpdatedHandler<CodeListType>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<CodeListType> event) {
				setCodelistType(event.getItem());
			}
		});
		
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}
		});
		
	}
	

	@Override
	public FlowNode<WizardStep> selectNode(List<FlowNode<WizardStep>> children) {
		
		for (FlowNode<WizardStep> child:children) if (child.getItem().getId().equals(nextStep.getId())) return child;
		
		return null;
	}
	
	public void switchToNormal()
	{
		this.nextStep = oldNextStep;
	}


	protected void reset() {
		nextStep = sdmxStep;		
	}


	protected void setCodelistType(CodeListType codeListType) {
		Log.trace("TypeNodeSelector updating next to "+codeListType);
		switch (codeListType) {
			case CSV: nextStep = csvStep; break;
			case SDMX: nextStep = sdmxStep; break;
		}
		switchUpdated();
	}
}
