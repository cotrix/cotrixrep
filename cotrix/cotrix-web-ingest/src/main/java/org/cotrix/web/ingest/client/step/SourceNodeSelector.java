/**
 * 
 */
package org.cotrix.web.ingest.client.step;

import java.util.List;

import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.event.SourceTypeChangeEvent;
import org.cotrix.web.ingest.client.step.selection.SelectionStepPresenter;
import org.cotrix.web.ingest.client.step.upload.UploadStepPresenter;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.wizard.client.flow.FlowNode;
import org.cotrix.web.wizard.client.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class SourceNodeSelector extends AbstractNodeSelector<WizardStep> implements  ResetWizardHandler {
	
	protected static interface SourceNodeSelectorEventBinder extends EventBinder<SourceNodeSelector> {}

	protected SelectionStepPresenter channelStep;
	protected UploadStepPresenter uploadStep;
	protected WizardStep nextStep;
	
	@Inject
	public SourceNodeSelector(@ImportBus EventBus importBus, SelectionStepPresenter channelStep, UploadStepPresenter uploadStep)
	{
		this.channelStep = channelStep;
		this.uploadStep = uploadStep;
		this.nextStep = uploadStep;
		importBus.addHandler(ResetWizardEvent.TYPE, this);
	}
	
	@Inject
	private void bind(SourceNodeSelectorEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
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
	@EventHandler
	void onSourceTypeChange(SourceTypeChangeEvent event) {
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
