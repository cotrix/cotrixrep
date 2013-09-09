/**
 * 
 */
package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent.CodeListTypeUpdatedHandler;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.importwizard.client.flow.FlowNode;
import org.cotrix.web.importwizard.client.step.VisualWizardStep;
import org.cotrix.web.importwizard.client.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TypeNodeSelector extends AbstractNodeSelector<VisualWizardStep> implements CodeListTypeUpdatedHandler, ResetWizardHandler {
	
	protected WizardStep csvStep;
	protected WizardStep sdmxStep;
	protected WizardStep nextStep;
	
	public TypeNodeSelector(EventBus importBus, WizardStep csvStep, WizardStep sdmxStep)
	{
		this.csvStep = csvStep;
		this.sdmxStep = sdmxStep;
		this.nextStep = sdmxStep;
		importBus.addHandler(CodeListTypeUpdatedEvent.TYPE, this);
		importBus.addHandler(ResetWizardEvent.TYPE, this);
	}
	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public FlowNode<VisualWizardStep> selectNode(List<FlowNode<VisualWizardStep>> children) {
		
		for (FlowNode<VisualWizardStep> child:children) if (child.getItem().getId().equals(nextStep.getId())) return child;
		
		return null;
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
