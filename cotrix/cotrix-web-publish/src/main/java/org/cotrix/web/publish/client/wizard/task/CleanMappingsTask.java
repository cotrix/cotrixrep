/**
 * 
 */
package org.cotrix.web.publish.client.wizard.task;

import org.cotrix.web.publish.client.event.MappingsUpdatedEvent;
import org.cotrix.web.publish.client.event.PublishBus;
import org.cotrix.web.publish.client.wizard.PublishWizardAction;
import org.cotrix.web.publish.shared.AttributesMappings;
import org.cotrix.web.wizard.client.WizardAction;
import org.cotrix.web.wizard.client.step.TaskWizardStep;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class CleanMappingsTask implements TaskWizardStep {
	
	protected EventBus publishBus;
	
	@Inject
	public CleanMappingsTask(@PublishBus EventBus publishBus)
	{
		this.publishBus = publishBus;
	}

	@Override
	public String getId() {
		return "CleanMappingsTask";
	}

	@Override
	public boolean leave() {
		return true;
	}

	@Override
	public void run(final TaskCallBack callback) {
		
		publishBus.fireEventFromSource(new MappingsUpdatedEvent(new AttributesMappings()), CleanMappingsTask.this);
		callback.onSuccess(PublishWizardAction.NEXT);
	}
	
	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WizardAction getAction() {
		// TODO Auto-generated method stub
		return null;
	}

}
