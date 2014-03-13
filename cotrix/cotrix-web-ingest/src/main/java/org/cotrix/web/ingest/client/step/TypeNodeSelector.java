/**
 * 
 */
package org.cotrix.web.ingest.client.step;

import java.util.List;

import org.cotrix.web.ingest.client.event.CodeListTypeUpdatedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.csvpreview.CsvPreviewStepPresenter;
import org.cotrix.web.ingest.client.task.MappingsLoadingTask;
import org.cotrix.web.wizard.client.event.ResetWizardEvent;
import org.cotrix.web.wizard.client.event.ResetWizardEvent.ResetWizardHandler;
import org.cotrix.web.wizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.wizard.client.flow.FlowNode;
import org.cotrix.web.wizard.client.step.WizardStep;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TypeNodeSelector extends AbstractNodeSelector<WizardStep> implements ResetWizardHandler {
	
	protected static interface TypeNodeSelectorEventBinder extends EventBinder<TypeNodeSelector> {}
	
	@Inject
	protected CsvPreviewStepPresenter csvStep;
	@Inject
	protected MappingsLoadingTask sdmxStep;
	
	protected WizardStep nextStep;
	
	@Inject
	private void setDefault() {
		this.nextStep = csvStep;
	}
	
	@Inject
	private void bind(TypeNodeSelectorEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
		importEventBus.addHandler(ResetWizardEvent.TYPE, this);
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
	public void onResetWizard(ResetWizardEvent event) {
		nextStep = sdmxStep;		
	}

	@EventHandler
	void onCodeListTypeUpdated(CodeListTypeUpdatedEvent event) {
		Log.trace("TypeNodeSelector updating next to "+event.getCodeListType()+" event: "+event.toDebugString());
		switch (event.getCodeListType()) {
			case CSV: nextStep = csvStep; break;
			case SDMX: nextStep = sdmxStep; break;
		}
		switchUpdated();

	}
}
