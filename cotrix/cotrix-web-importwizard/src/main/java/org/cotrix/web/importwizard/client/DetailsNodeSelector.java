/**
 * 
 */
package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.importwizard.client.flow.FlowNode;
import org.cotrix.web.importwizard.client.step.WizardStep;
import org.cotrix.web.importwizard.client.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.importwizard.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.importwizard.client.task.RetrieveAssetTask;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DetailsNodeSelector extends AbstractNodeSelector<WizardStep> implements ResetWizardHandler, ValueChangeHandler<WizardStep> {
	
	protected WizardStep nextStep;
	
	@Inject
	protected RetrieveAssetTask retrieveStep;
	
	@Inject
	protected CodelistDetailsStepPresenter codelistDetailsStep;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetailsStep;
	
	@Inject
	public DetailsNodeSelector(@ImportBus EventBus importBus, RetrieveAssetTask retrieveStep)
	{
		this.retrieveStep = retrieveStep;
		this.nextStep = retrieveStep;
		importBus.addHandler(ResetWizardEvent.TYPE, this);
		importBus.addHandler(ValueChangeEvent.getType(), this);
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
		this.nextStep = repositoryDetailsStep;
	}
	
	public void switchToAssetRetrieve()
	{
		this.nextStep = retrieveStep;
	}
	
	public boolean toDetails()
	{
		return nextStep == repositoryDetailsStep || nextStep == codelistDetailsStep;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onResetWizard(ResetWizardEvent event) {
		nextStep = retrieveStep;
	}

	@Override
	public void onValueChange(ValueChangeEvent<WizardStep> event) {
		if (event.getValue().getId().equals(codelistDetailsStep.getId()) 
				|| event.getValue().getId().equals(repositoryDetailsStep.getId())) switchToAssetRetrieve();
	}

}