package org.cotrix.web.publish.client.event;

import org.cotrix.web.share.client.event.GenericEventTypeManager;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ItemSelectedEvent<T> extends GwtEvent<ItemSelectedEvent.ItemSelectedHandler<T>> {

	/**
	 * Handler type.
	 */
	private static GenericEventTypeManager<ItemSelectedEvent.ItemSelectedHandler<?>> TYPE_MANAGER = new GenericEventTypeManager<ItemSelectedEvent.ItemSelectedHandler<?>>();

	public interface ItemSelectedHandler<T> extends EventHandler {
		void onItemSelected(ItemSelectedEvent<T> event);
	}

	protected T item;

	public ItemSelectedEvent(T item) {
		assert item != null;
		this.item = item;
	}

	public T getItem(){
		return item;
	}

	@Override
	protected void dispatch(ItemSelectedHandler<T> handler) {
		handler.onItemSelected(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final Type<ItemSelectedHandler<T>> getAssociatedType() {
		return (Type) TYPE_MANAGER.getType(item.getClass());
	}

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<ItemSelectedHandler<?>> getType(Class<?> clazz) {
		return TYPE_MANAGER.getType(clazz);
	}
}
