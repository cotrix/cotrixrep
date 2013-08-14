/**
 * 
 */
package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent;
import org.cotrix.web.importwizard.client.event.CodeListTypeUpdatedEvent.CodeListTypeUpdatedHandler;
import org.cotrix.web.importwizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.importwizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.importwizard.client.flow.FlowNode;
import org.cotrix.web.importwizard.client.step.WizardStep;
import org.cotrix.web.importwizard.client.step.codelistdetails.CodelistDetailsStepPresenter;
import org.cotrix.web.importwizard.client.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.importwizard.client.step.repositorydetails.RepositoryDetailsStepPresenter;
import org.cotrix.web.importwizard.client.step.sdmxmapping.SdmxMappingStepPresenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DetailsNodeSelector extends AbstractNodeSelector<WizardStep> implements CodeListTypeUpdatedHandler, ResetWizardHandler, ValueChangeHandler<WizardStep> {
	
	protected WizardStep nextStep;
	protected WizardStep oldNextStep;
	
	@Inject
	protected CsvMappingStepPresenter csvStep;
	
	@Inject
	protected SdmxMappingStepPresenter sdmxStep;
	
	@Inject
	protected CodelistDetailsStepPresenter codelistDetailsStep;
	
	@Inject
	protected RepositoryDetailsStepPresenter repositoryDetailsStep;
	
	@Inject
	public DetailsNodeSelector(@ImportBus EventBus importBus, CsvMappingStepPresenter csvStep)
	{
		this.csvStep = csvStep;
		this.nextStep = csvStep;
		importBus.addHandler(CodeListTypeUpdatedEvent.TYPE, this);
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
		this.oldNextStep = nextStep; 
		this.nextStep = codelistDetailsStep;
	}
	
	public void switchToRepositoryDetails()
	{
		this.oldNextStep = nextStep; 
		this.nextStep = repositoryDetailsStep;
	}
	
	public void switchToNormal()
	{
		this.nextStep = oldNextStep;
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


	@Override
	public void onValueChange(ValueChangeEvent<WizardStep> event) {
		if (event.getValue().getId().equals(codelistDetailsStep.getId()) 
				|| event.getValue().getId().equals(repositoryDetailsStep.getId())) switchToNormal();
	}

}
