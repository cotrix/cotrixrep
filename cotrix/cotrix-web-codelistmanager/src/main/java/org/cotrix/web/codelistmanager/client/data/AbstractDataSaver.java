/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.codelistmanager.client.data.event.DataSaveFailedEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataSavedEvent;
import org.cotrix.web.codelistmanager.client.data.event.EditType;
import org.cotrix.web.codelistmanager.client.data.event.SavingDataEvent;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.codelistmanager.shared.modify.ModifyCommand;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.StatusUpdatedEvent;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.shared.GwtEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractDataSaver<T> implements DataEditHandler<T> {
	
	@Inject
	protected ModifyCommandSequencer commandSequencer;

	@ManagerBus
	@Inject
	protected EventBus bus;
	
	@CotrixBus
	@Inject
	protected EventBus cotrixBus;
	
	/** 
	 * {@inheritDoc}
	 */
	protected void fireEvent(GwtEvent<?> event) {
		bus.fireEvent(event);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void onDataEdit(DataEditEvent<T> event) {
		fireEvent(new SavingDataEvent());
		cotrixBus.fireEvent(new StatusUpdatedEvent("Saving ..."));
		ModifyCommand command = generateCommand(event.getEditType(), event.getData());
		commandSequencer.enqueueCommand(command, new Callback<Void, Throwable>() {
			
			@Override
			public void onSuccess(Void result) {
				fireEvent(new DataSavedEvent());
				cotrixBus.fireEvent(new StatusUpdatedEvent("All saved"));
			}
			
			@Override
			public void onFailure(Throwable reason) {
				fireEvent(new DataSaveFailedEvent(reason));
			}
		});
	}
	
	public abstract ModifyCommand generateCommand(EditType editType, T data);
}
