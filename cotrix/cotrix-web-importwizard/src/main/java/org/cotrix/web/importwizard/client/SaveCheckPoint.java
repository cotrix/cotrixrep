/**
 * 
 */
package org.cotrix.web.importwizard.client;

import org.cotrix.web.importwizard.client.event.ImportBus;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent;
import org.cotrix.web.importwizard.client.event.SaveEvent;
import org.cotrix.web.importwizard.client.event.ImportProgressEvent.ImportProgressHandler;
import org.cotrix.web.importwizard.client.flow.CheckPointNode.CheckPointHandler;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class SaveCheckPoint implements CheckPointHandler, ImportProgressHandler {
	
	@Inject
	@ImportBus 
	EventBus importEventBus;
	
	protected boolean importComplete = false;
	protected boolean eventFired = false;
	
	@Override
	public boolean check() {
		if (!eventFired) {
			bind();
			fireSaveEvent();
			eventFired = true;
		}
		return importComplete;
	}
	
	protected void fireSaveEvent()
	{
		importEventBus.fireEvent(new SaveEvent());
	}
	
	protected void bind()
	{
		importEventBus.addHandler(ImportProgressEvent.TYPE, this);
	}

	@Override
	public void onImportProgress(ImportProgressEvent event) {
		importComplete = event.getProgress().isComplete();
	}

}
