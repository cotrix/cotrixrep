/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.codelistmanager.client.data.event.DataSaveFailedEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataSavedEvent;
import org.cotrix.web.codelistmanager.client.data.event.SavingDataEvent;
import org.cotrix.web.codelistmanager.client.event.ManagerBus;
import org.cotrix.web.share.client.event.CotrixBus;
import org.cotrix.web.share.client.event.StatusUpdatedEvent;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public abstract class AbstractDataSaver<T> implements DataEditHandler<T> {

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
		//TODO retry policy
		save(event.getData(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				fireEvent(new DataSaveFailedEvent(caught));
			}

			@Override
			public void onSuccess(Void result) {
				fireEvent(new DataSavedEvent());
				cotrixBus.fireEvent(new StatusUpdatedEvent("All saved"));
			}
		});
	}
	
	public abstract void save(T data, AsyncCallback<Void> callback);
}
