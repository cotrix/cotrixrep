/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import java.util.List;

import org.cotrix.web.publish.client.event.ItemUpdatedEvent;
import org.cotrix.web.publish.shared.Destination;
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
public class DestinationNodeSelector extends AbstractNodeSelector<WizardStep> {
	
	protected WizardStep channel;
	protected WizardStep file;
	protected WizardStep nextStep;
	

	public DestinationNodeSelector(EventBus publishBus, WizardStep channel, WizardStep file)
	{
		this.channel = channel;
		this.file = file;
		this.nextStep = file;
		
		bind(publishBus);
	}
	
	protected void bind(EventBus publishBus)
	{
		publishBus.addHandler(ItemUpdatedEvent.getType(Destination.class), new ItemUpdatedEvent.ItemUpdatedHandler<Destination>() {

			@Override
			public void onItemUpdated(ItemUpdatedEvent<Destination> event) {
				Log.trace("onItemUpdated DestinationType "+event.getItem());
				setDestination(event.getItem());
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
		Log.trace("DestinationNodeSelector nextStep: "+nextStep.getId()+" proposed children: "+children);
		for (FlowNode<WizardStep> child:children)  {
			Log.trace("checking for "+child.getItem().getId());
			if (child.getItem().getId().equals(nextStep.getId())) {
				Log.trace("returning "+nextStep.getId());
				return child;
			}
		}
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	public void setDestination(Destination destination) {
		Log.trace("switching destination to "+destination);
		switch (destination) {
			case CHANNEL:nextStep = channel; break;
			case FILE: nextStep = file; break;
		}
		
		Log.trace("nextStep: "+nextStep.getId());
		switchUpdated();
	}


	/** 
	 * {@inheritDoc}
	 */
	public void reset() {
		nextStep = file;		
	}

}
