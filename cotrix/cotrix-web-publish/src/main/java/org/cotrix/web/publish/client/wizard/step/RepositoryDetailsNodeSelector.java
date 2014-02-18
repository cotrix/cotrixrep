/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import java.util.List;

import org.cotrix.web.publish.client.event.ItemDetailsRequestedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.task.RetrieveMappingsTask;
import org.cotrix.web.publish.client.wizard.task.RetrieveRepositoryDetailsTask;
import org.cotrix.web.publish.shared.UIRepository;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.wizard.client.flow.FlowNode;
import org.cotrix.web.wizard.client.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class RepositoryDetailsNodeSelector extends AbstractNodeSelector<WizardStep> {
	
	protected WizardStep nextStep;
	
	@Inject
	protected RetrieveRepositoryDetailsTask retrieveStep;
	
	protected RetrieveMappingsTask retrieveMappings;
	
	@Inject
	public RepositoryDetailsNodeSelector(@PublishBus EventBus publishBus, RetrieveMappingsTask retrieveMappings)
	{
		this.retrieveMappings = retrieveMappings;
		this.nextStep = retrieveMappings;
		bind(publishBus);
	}
	
	protected void bind(EventBus publishBus) {
		publishBus.addHandler(ResetWizardEvent.TYPE, new ResetWizardHandler(){

			@Override
			public void onResetWizard(ResetWizardEvent event) {
				reset();
			}
			
		});
		publishBus.addHandler(ValueChangeEvent.getType(), new ValueChangeHandler<WizardStep>(){

			@Override
			public void onValueChange(ValueChangeEvent<WizardStep> event) {
				stepChanged(event.getValue());
			}
			
		});
		publishBus.addHandler(ItemDetailsRequestedEvent.getType(UIRepository.class), new ItemDetailsRequestedEvent.ItemDetailsRequestedHandler<UIRepository>() {

			@Override
			public void onItemDetailsRequest(ItemDetailsRequestedEvent<UIRepository> event) {
				switchToRepositoryDetails();		
			}
		});
	}
	

	@Override
	public FlowNode<WizardStep> selectNode(List<FlowNode<WizardStep>> children) {
		Log.trace("RepositoryDetailsNodeSelector nextStep: "+nextStep.getId()+" proposed children: "+children);
		for (FlowNode<WizardStep> child:children)  {
			Log.trace("checking for "+child.getItem().getId());
			if (child.getItem().getId().equals(nextStep.getId())) {
				Log.trace("returning "+nextStep.getId());
				return child;
			}
		}
		
		return null;
	}
	
	protected void switchToRepositoryDetails()
	{
		Log.trace("switchToRepositoryDetails");
		this.nextStep = retrieveStep;
	}
	
	public boolean isSwitchedToCodeListDetails() {
		return nextStep == retrieveStep;
	}

	public void reset() {
		Log.trace("reset");
		nextStep = retrieveMappings;
	}

	public void stepChanged(WizardStep step) {
		if (step.getId().equals(retrieveStep.getId())) {
			nextStep = retrieveMappings;
		}
	}

}
