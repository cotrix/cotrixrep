/**
 * 
 */
package org.cotrix.web.ingest.client.step;

import java.util.List;

import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.preview.PreviewStepPresenter;
import org.cotrix.web.ingest.client.step.summary.SummaryStepPresenter;
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
public class AssetPreviewNodeSelector extends AbstractNodeSelector<WizardStep> implements ResetWizardHandler, ValueChangeHandler<WizardStep> {
	
	protected WizardStep nextStep;
	
	@Inject
	protected SummaryStepPresenter summaryStep;
	
	@Inject
	protected PreviewStepPresenter previewStep;
	
	@Inject
	public AssetPreviewNodeSelector(@ImportBus EventBus importBus, SummaryStepPresenter summaryStep)
	{
		this.summaryStep = summaryStep;
		this.nextStep = summaryStep;
		importBus.addHandler(ResetWizardEvent.TYPE, this);
		importBus.addHandler(ValueChangeEvent.getType(), this);
	}
	

	@Override
	public FlowNode<WizardStep> selectNode(List<FlowNode<WizardStep>> children) {
		
		for (FlowNode<WizardStep> child:children) if (child.getItem().getId().equals(nextStep.getId())) return child;
		
		return null;
	}
	
	public void switchToPreview()
	{
		this.nextStep = previewStep;
	}
	
	public void switchToSummary()
	{
		this.nextStep = summaryStep;
	}
	
	public boolean toDetails()
	{
		return nextStep == previewStep;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onResetWizard(ResetWizardEvent event) {
		nextStep = summaryStep;
	}

	@Override
	public void onValueChange(ValueChangeEvent<WizardStep> event) {
		if (event.getValue().getId().equals(previewStep.getId())) switchToSummary();
	}

}
