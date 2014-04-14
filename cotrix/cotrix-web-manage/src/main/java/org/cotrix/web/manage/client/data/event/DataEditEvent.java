package org.cotrix.web.manage.client.data.event;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 * @param <T>
 */
public class DataEditEvent<T> extends GwtEvent<DataEditEvent.DataEditHandler<T>> {

	private static Map<Class<?>, Type<DataEditHandler<?>>> TYPES = new HashMap<Class<?>, Type<DataEditHandler<?>>>();

	public static Type<DataEditHandler<?>> getType(Class<?> clazz) {

		if (!TYPES.containsKey(clazz)) {
			TYPES.put(clazz, new Type<DataEditHandler<?>>());
		}

		return TYPES.get(clazz);
	}


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
		return (Type) DataEditEvent.getType(this.data.getClass());
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataEditEvent [editType=");
		builder.append(editType);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}
}
