/**
 * 
 */
package org.cotrix.web.importwizard.client;

import java.util.List;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.SourceTypeChangeEvent;
import org.cotrix.web.importwizard.client.event.SourceTypeChangeEvent.SourceTypeChangeHandler;
import org.cotrix.web.importwizard.client.flow.AbstractNodeSelector;
import org.cotrix.web.importwizard.client.flow.FlowNode;
import org.cotrix.web.importwizard.client.step.WizardStep;
import org.cotrix.web.importwizard.client.step.channel.ChannelStepPresenter;
import org.cotrix.web.importwizard.client.step.upload.UploadStepPresenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SourceNodeSelector extends AbstractNodeSelector<WizardStep> implements SourceTypeChangeHandler {
	
	protected ChannelStepPresenter channelStep;
	protected UploadStepPresenter uploadStep;
	protected WizardStep nextStep;
	
	@Inject
	public SourceNodeSelector(@ImportBus EventBus importBus, ChannelStepPresenter channelStep, UploadStepPresenter uploadStep)
	{
		importBus.addHandler(SourceTypeChangeEvent.TYPE, this);
		this.channelStep = channelStep;
		this.uploadStep = uploadStep;
		this.nextStep = uploadStep;
	}
	

	@Override
	public FlowNode<WizardStep> selectNode(List<FlowNode<WizardStep>> children) {
		
		for (FlowNode<WizardStep> child:children) if (child.getItem().getId().equals(nextStep.getId())) return child;
		
		return null;
	}

	@Override
	public void onSourceTypeChange(SourceTypeChangeEvent event) {
		Log.trace("switching source to "+event.getSourceType());
		switch (event.getSourceType()) {
			case CHANNEL:nextStep = channelStep; break;
			case FILE: nextStep = uploadStep; break;
		}
		switchUpdated();
	}

}
