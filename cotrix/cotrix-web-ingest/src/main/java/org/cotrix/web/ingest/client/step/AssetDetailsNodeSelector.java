/**
 * 
 */
package org.cotrix.web.ingest.client.step;

import java.util.List;

import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.ingest.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.ingest.client.task.RetrieveAssetTask;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.wizard.client.flow.FlowNode;
import org.cotrix.web.wizard.client.step.WizardStep;

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
public class AssetDetailsNodeSelector extends AbstractNodeSelector<WizardStep> implements ResetWizardHandler, ValueChangeHandler<WizardStep> {
	
	protected WizardStep nextStep;
	
	@Inject
	protected RetrieveAssetTask retrieveStep;
	
	@Inject
	protected CodelistDetailsStepPresenter codelistDetailsStep;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetailsStep;
	
	@Inject
	public AssetDetailsNodeSelector(@ImportBus EventBus importBus, RetrieveAssetTask retrieveStep)
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
