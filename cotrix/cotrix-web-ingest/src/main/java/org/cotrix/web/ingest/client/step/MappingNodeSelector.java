/**
 * 
 */
package org.cotrix.web.ingest.client.step;

import java.util.List;

import org.cotrix.web.ingest.client.event.AssetTypeUpdatedEvent;
import org.cotrix.web.ingest.client.event.ImportBus;
import org.cotrix.web.ingest.client.step.csvmapping.CsvMappingStepPresenter;
import org.cotrix.web.ingest.client.step.sdmxmapping.SdmxMappingStepPresenter;
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
public class MappingNodeSelector extends AbstractNodeSelector<WizardStep> implements ResetWizardHandler {
	
	protected static interface MappingNodeSelectorEventBinder extends EventBinder<MappingNodeSelector> {}

	protected WizardStep nextStep;
	protected WizardStep oldNextStep;
	
	protected CsvMappingStepPresenter csvStep;
	
	@Inject
	protected SdmxMappingStepPresenter sdmxStep;
	
	@Inject
	public MappingNodeSelector(@ImportBus EventBus importBus, CsvMappingStepPresenter csvStep)
	{
		this.csvStep = csvStep;
		this.nextStep = csvStep;
		importBus.addHandler(ResetWizardEvent.TYPE, this);
	}
	
	@Inject
	private void bind(MappingNodeSelectorEventBinder binder, @ImportBus EventBus importEventBus) {
		binder.bindEventHandlers(this, importEventBus);
	}	

	@Override
	public FlowNode<WizardStep> selectNode(List<FlowNode<WizardStep>> children) {
		
		for (FlowNode<WizardStep> child:children) if (child.getItem().getId().equals(nextStep.getId())) return child;
		
		return null;
	}
	
	public void switchToNormal()
	{
		this.nextStep = oldNextStep;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onResetWizard(ResetWizardEvent event) {
		nextStep = sdmxStep;		
	}


	@EventHandler
	void onCodeListTypeUpdated(AssetTypeUpdatedEvent event) {
		Log.trace("TypeNodeSelector updating next to "+event.getAssetType()+" event: "+event.toDebugString());
		switch (event.getAssetType()) {
			case CSV: nextStep = csvStep; break;
			case SDMX: nextStep = sdmxStep; break;
		}
		switchUpdated();
	}
}
