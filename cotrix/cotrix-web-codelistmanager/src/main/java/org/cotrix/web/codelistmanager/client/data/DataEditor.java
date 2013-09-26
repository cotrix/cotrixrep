/**
 * 
 */
package org.cotrix.web.codelistmanager.client.data;

import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.DataEditHandler;
import org.cotrix.web.codelistmanager.client.data.event.DataEditEvent.HasDataEditHandlers;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DataEditor<T> implements HasDataEditHandlers<T> {
	
	protected HandlerManager handlerManager = new HandlerManager(this);
	
	public void edited(T data)
	{
		fireEvent(new DataEditEvent<T>(data));
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addDataEditHandler(DataEditHandler<T> handler) {
		return handlerManager.addHandler(DataEditEvent.TYPE, handler);
	}
}
