/**
 * 
 */
package org.cotrix.web.importwizard.client.step;

import java.util.List;

import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent.CodeListTypeUpdatedHandler;
import org.cotrix.web.importwizard.client.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.importwizard.client.step.sdmxmapping.SdmxMappingStepPresenter;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
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
public class MappingNodeSelector extends AbstractNodeSelector<WizardStep> implements CodeListTypeUpdatedHandler, ResetWizardHandler {
	
	protected WizardStep nextStep;
	protected WizardStep oldNextStep;
	
	protected CsvMappingStepPresenter csvStep;
	
	@Inject
	protected SdmxMappingStepPresenter sdmxStep;
	
	@Inject
	public MappingNodeSelector(@ImportBus EventBus importBus, CsvMappingStepPresenter csvStep)
	{
		this.csvStep = csvStep;
		this.nextStep = csvStep;
		importBus.addHandler(CodeListTypeUpdatedEvent.TYPE, this);
		importBus.addHandler(ResetWizardEvent.TYPE, this);
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

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onResetWizard(ResetWizardEvent event) {
		nextStep = sdmxStep;		
	}


	@Override
	public void onCodeListTypeUpdated(CodeListTypeUpdatedEvent event) {
		Log.trace("TypeNodeSelector updating next to "+event.getCodeListType()+" event: "+event.toDebugString());
		switch (event.getCodeListType()) {
			case CSV: nextStep = csvStep; break;
			case SDMX: nextStep = sdmxStep; break;
		}
		switchUpdated();
	}
}
