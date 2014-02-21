/**
 * 
 */
package org.cotrix.web.importwizard.client.step;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.SourceTypeChangeEvent;
import org.cotrix.web.importwizard.client.event.SourceTypeChangeEvent.SourceTypeChangeHandler;
import org.cotrix.web.importwizard.client.step.selection.SelectionStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent;
import org.cotrix.web.share.client.wizard.event.ResetWizardEvent.ResetWizardHandler;
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
public class SourceNodeSelector extends AbstractNodeSelector<WizardStep> implements SourceTypeChangeHandler, ResetWizardHandler {
	
	protected SelectionStepPresenter channelStep;
	protected UploadStepPresenter uploadStep;
	protected WizardStep nextStep;
	
	@Inject
	public SourceNodeSelector(@ImportBus EventBus importBus, SelectionStepPresenter channelStep, UploadStepPresenter uploadStep)
	{
		this.channelStep = channelStep;
		this.uploadStep = uploadStep;
		this.nextStep = uploadStep;
		importBus.addHandler(SourceTypeChangeEvent.TYPE, this);
		importBus.addHandler(ResetWizardEvent.TYPE, this);
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
	@Override
	public void onSourceTypeChange(SourceTypeChangeEvent event) {
		Log.trace("switching source to "+event.getSourceType());
		switch (event.getSourceType()) {
			case CHANNEL:nextStep = channelStep; break;
			case FILE: nextStep = uploadStep; break;
		}
		switchUpdated();
	}


	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onResetWizard(ResetWizardEvent event) {
		nextStep = uploadStep;		
	}

}
