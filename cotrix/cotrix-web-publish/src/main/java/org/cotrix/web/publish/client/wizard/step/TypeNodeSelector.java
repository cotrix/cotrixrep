/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import java.util.List;

import org.cotrix.web.common.shared.Format;
import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.wizard.client.flow.FlowNode;
import org.cotrix.web.wizard.client.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TypeNodeSelector extends AbstractNodeSelector<WizardStep> {
	
	private WizardStep csvStep;
	private WizardStep sdmxStep;
	private WizardStep cometStep;
	private WizardStep nextStep;
	
	public TypeNodeSelector(EventBus publishBus, WizardStep csvStep, WizardStep sdmxStep, WizardStep cometStep)
	{
		this.csvStep = csvStep;
		this.sdmxStep = sdmxStep;
		this.cometStep = cometStep;
		this.nextStep = csvStep;
		bind(publishBus);
	}
	
	protected void bind(EventBus publishBus)
	{
		publishBus.addHandler(ItemUpdatedEvent.getType(Format.class), new ItemUpdatedEvent.ItemUpdatedHandler<Format>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<Format> event) {
				setType(event.getItem());				
			}
		});
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardEvent.ResetWizardHandler() {
			
			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}
		});
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public FlowNode<WizardStep> selectNode(List<FlowNode<WizardStep>> children) {
		
		for (FlowNode<WizardStep> child:children) if (child.getItem().getId().equals(nextStep.getId())) return child;
		
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public void reset() {
		nextStep = csvStep;		
	}


	public void setType(Format type) {
		Log.trace("TypeNodeSelector updating next to "+type);
		switch (type) {
			case CSV: nextStep = csvStep; break;
			case SDMX: nextStep = sdmxStep; break;
			case COMET: nextStep = cometStep; break;
		}
		switchUpdated();

	}

}
