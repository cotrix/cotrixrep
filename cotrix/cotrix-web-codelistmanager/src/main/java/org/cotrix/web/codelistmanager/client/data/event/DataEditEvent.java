package org.cotrix.web.codelistmanager.client.data.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class DataEditEvent<T> extends GwtEvent<DataEditEvent.DataEditHandler<T>> {

	public static Type<DataEditHandler<?>> TYPE = new Type<DataEditHandler<?>>();
	
	protected EditType editType;
	private T data;
	
	public interface DataEditHandler<T> extends EventHandler {
		void onDataEdit(DataEditEvent<T> event);
	}

	public interface HasDataEditHandlers<T> extends HasHandlers {
		/**
		 * Adds a {@link DataEditEvent} handler.
		 * 
		 * @param handler the handler
		 * @return the registration for the event
		 */
		HandlerRegistration addDataEditHandler(DataEditHandler<T> handler);
	}

	public DataEditEvent(T data, EditType editType) {
		this.data = data;
		this.editType = editType;
	}

	public T getData() {
		return data;
	}

	/**
	 * @return the editType
	 */
	public EditType getEditType() {
		return editType;
	}

	@Override
	protected void dispatch(DataEditHandler<T> handler) {
		handler.onDataEdit(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Type<DataEditHandler<T>> getAssociatedType() {
		return (Type)TYPE;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Type<DataEditHandler> getType() {
		return (Type)TYPE;
	}
}
