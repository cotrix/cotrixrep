/**
 * 
 */
package org.cotrix.web.publish.client.wizard.step;

import java.util.List;

import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.publish.client.wizard.step.destinationselection.DestinationSelectionStepPresenter;
import org.cotrix.web.publish.client.wizard.task.RetrieveMetadataTask;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.share.client.wizard.flow.AbstractNodeSelector;
import org.cotrix.web.share.client.wizard.flow.FlowNode;
import org.cotrix.web.share.client.wizard.step.WizardStep;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DetailsNodeSelector extends AbstractNodeSelector<WizardStep> {
	
	protected WizardStep nextStep;
	
	@Inject
	protected RetrieveMetadataTask retrieveStep;
	
	@Inject
	protected CodelistDetailsStepPresenter codelistDetailsStep;
	
	@Inject
	protected DestinationSelectionStepPresenter destinationSelectionStep;
	
	@Inject
	public DetailsNodeSelector(@PublishBus EventBus publishBus, RetrieveMetadataTask retrieveStep)
	{
		this.retrieveStep = retrieveStep;
		this.nextStep = retrieveStep;
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
	}
	

	@Override
	public FlowNode<WizardStep> selectNode(List<FlowNode<WizardStep>> children) {
		
		for (FlowNode<WizardStep> child:children) if (child.getItem().getId().equals(nextStep.getId())) return child;
		
		return null;
	}
	
	public void switchToCodeListDetails()
	{
		this.nextStep = codelistDetailsStep;
	}
	
	public void switchToRepositoryDetails()
	{
		this.nextStep = destinationSelectionStep;
	}
	
	public void switchToMetadataRetrieve()
	{
		this.nextStep = retrieveStep;
	}
	
	public boolean toDetails()
	{
		return nextStep == destinationSelectionStep || nextStep == codelistDetailsStep;
	}

	public void reset() {
		nextStep = retrieveStep;
	}

	public void stepChanged(WizardStep step) {
		if (step.getId().equals(codelistDetailsStep.getId()) 
				|| step.getId().equals(destinationSelectionStep.getId())) switchToMetadataRetrieve();
	}

}
