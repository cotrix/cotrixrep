package org.cotrix.web.publish.client.event;

import org.cotrix.web.common.client.event.GenericEventTypeManager;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ItemUpdatedEvent<T> extends GwtEvent<ItemUpdatedEvent.ItemUpdatedHandler<T>> {

	/**
	 * Handler type.
	 */
	private static GenericEventTypeManager<ItemUpdatedEvent.ItemUpdatedHandler<?>> TYPE_MANAGER = new GenericEventTypeManager<ItemUpdatedEvent.ItemUpdatedHandler<?>>();

	public interface ItemUpdatedHandler<T> extends EventHandler {
		void onItemUpdated(ItemUpdatedEvent<T> event);
	}

	protected T item;

	public ItemUpdatedEvent(T item) {
		assert item != null;
		this.item = item;
	}

	public T getItem(){
		return item;
	}

	@Override
	protected void dispatch(ItemUpdatedHandler<T> handler) {
		handler.onItemUpdated(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final Type<ItemUpdatedHandler<T>> getAssociatedType() {
		return (Type) TYPE_MANAGER.getType(item.getClass());
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<ItemUpdatedHandler<?>> getType(Class<?> clazz) {
		return TYPE_MANAGER.getType(clazz);
	}
}
