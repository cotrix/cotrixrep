/**
 * 
 */
package org.cotrix.web.common.client.event;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GenericEvent<T> extends GwtEvent<GenericHandler<T>> {
	
	private static Map<Class<?>, Type<GenericHandler<?>>> TYPES = new HashMap<Class<?>, Type<GenericHandler<?>>>();

	public static Type<GenericHandler<?>> getType(Class<?> clazz) {

		if (!TYPES.containsKey(clazz)) {
			TYPES.put(clazz, new Type<GenericHandler<?>>());
		}

		return TYPES.get(clazz);
	}

	private T resource;

	public GenericEvent(T resource) {
		this.resource = resource;
	}
	
	@SuppressWarnings("all")
	@Override
	public Type<GenericHandler<T>> getAssociatedType() {
		return (Type) GenericEvent.getType(this.resource.getClass());
	}
	
	public T getResource() {
		return this.resource;
	}
	
	@Override
	protected void dispatch(GenericHandler<T> handler) {
		handler.onGenericEvent(this);
	}
}



