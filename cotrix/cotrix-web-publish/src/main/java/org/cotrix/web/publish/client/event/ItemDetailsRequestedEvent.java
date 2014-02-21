package org.cotrix.web.publish.client.event;

import org.cotrix.web.common.client.event.GenericEventTypeManager;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ItemDetailsRequestedEvent<T> extends GwtEvent<ItemDetailsRequestedEvent.ItemDetailsRequestedHandler<T>> {

	/**
	 * Handler type.
	 */
	private static GenericEventTypeManager<ItemDetailsRequestedHandler<?>> TYPE_MANAGER = new GenericEventTypeManager<ItemDetailsRequestedHandler<?>>();

	public interface ItemDetailsRequestedHandler<T> extends EventHandler {
		void onItemDetailsRequest(ItemDetailsRequestedEvent<T> event);
	}

	protected T item;

	public ItemDetailsRequestedEvent(T item) {
		assert item != null;
		this.item = item;
	}

	public T getItem(){
		return item;
	}

	@Override
	protected void dispatch(ItemDetailsRequestedHandler<T> handler) {
		handler.onItemDetailsRequest(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final Type<ItemDetailsRequestedHandler<T>> getAssociatedType() {
		return (Type) TYPE_MANAGER.getType(item.getClass());
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<ItemDetailsRequestedHandler<?>> getType(Class<?> clazz) {
		return TYPE_MANAGER.getType(clazz);
	}
}
