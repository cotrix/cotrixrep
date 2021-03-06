/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import java.util.List;

import org.cotrix.web.publish.client.event.ItemDetailsRequestedEvent;
import org.cotrix.web.publish.client.event.ItemSelectedEvent;
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
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class RepositoryDetailsNodeSelector extends AbstractNodeSelector<WizardStep> {
	
	private WizardStep nextStep;
	
	private RetrieveRepositoryDetailsTask retrieveStep;
	
	private WizardStep retrieveMappings;
	
	private WizardStep formatStep;
	
	private WizardStep nextRepositoryStep;
	
	public RepositoryDetailsNodeSelector(EventBus publishBus, RetrieveRepositoryDetailsTask retrieveStep, WizardStep retrieveMappings, WizardStep formatStep)
	{
		this.retrieveStep = retrieveStep;
		this.retrieveMappings = retrieveMappings;
		this.formatStep = formatStep;
		this.nextRepositoryStep = retrieveMappings;
		this.nextStep = nextRepositoryStep;
		bind(publishBus);
	}
	
	private void bind(EventBus publishBus) {
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
		publishBus.addHandler(ItemSelectedEvent.getType(UIRepository.class), new ItemSelectedEvent.ItemSelectedHandler<UIRepository>(){

			@Override
			public void onItemSelected(ItemSelectedEvent<UIRepository> event) {
				setRepository(event.getItem());		
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
	
	private void switchToRepositoryDetails()
	{
		Log.trace("switchToRepositoryDetails");
		this.nextStep = retrieveStep;
	}
	
	public boolean isSwitchedToCodeListDetails() {
		return nextStep == retrieveStep;
	}
	
	public void setRepository(UIRepository repository) {
		nextRepositoryStep = repository.getAvailableFormats().size()>1?formatStep:retrieveMappings;
		nextStep = nextRepositoryStep;
		Log.trace("repository selected: "+repository+" updated nextRepositoryNode to: "+nextRepositoryStep);
	}

	public void reset() {
		Log.trace("reset");
		nextRepositoryStep = retrieveMappings;
		nextStep = nextRepositoryStep;
	}

	public void stepChanged(WizardStep step) {
		Log.trace("stepChanged step: "+step);
		if (step.getId().equals(retrieveStep.getId())) {
			nextStep = nextRepositoryStep;
		}
	}

}
