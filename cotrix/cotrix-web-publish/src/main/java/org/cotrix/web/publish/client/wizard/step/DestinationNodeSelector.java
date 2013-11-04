/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import java.util.List;

import org.cotrix.web.publish.client.event.DestinationType;
import org.cotrix.web.publish.client.event.DestinationTypeChangeEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.step.typeselection.TypeSelectionStepPresenter;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.flow.AbstractNodeSelector;
import org.cotrix.web.share.client.wizard.flow.FlowNode;
import org.cotrix.web.share.client.wizard.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DestinationNodeSelector extends AbstractNodeSelector<WizardStep> {
	
	protected TypeSelectionStepPresenter typeStep;
	//protected UploadStepPresenter uploadStep;
	protected WizardStep nextStep;
	
	@Inject
	public DestinationNodeSelector(@PublishBus EventBus publishBus, TypeSelectionStepPresenter typeStep/*, UploadStepPresenter uploadStep*/)
	{
		this.typeStep = typeStep;
		/*this.uploadStep = typeStep;*/
		this.nextStep = typeStep;
		
		bind(publishBus);
	}
	
	protected void bind(EventBus publishBus)
	{
		publishBus.addHandler(DestinationTypeChangeEvent.TYPE, new DestinationTypeChangeEvent.DestinationTypeChangeHandler() {
			
			@Override
			public void onDestinationTypeChange(DestinationTypeChangeEvent event) {
				setDestination(event.getDestinationType());
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
	public void setDestination(DestinationType destination) {
		Log.trace("switching destination to "+destination);
		switch (destination) {
			/*case CHANNEL:nextStep = typeStep; break;*/
			case FILE: nextStep = typeStep; break;
		}
		switchUpdated();
	}


	/** 
	 * {@inheritDoc}
	 */
	public void reset() {
		nextStep = typeStep;		
	}

}
