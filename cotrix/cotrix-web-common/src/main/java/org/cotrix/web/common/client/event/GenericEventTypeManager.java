/**
 * 
 */
package org.cotrix.web.common.client.event;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class GenericEventTypeManager<H extends EventHandler> {
	
	protected Map<Class<?>, Type<H>> TYPES = new HashMap<Class<?>, Type<H>>();

	public Type<H> getType(Class<?> clazz) {

		if (!TYPES.containsKey(clazz)) {
			TYPES.put(clazz, new Type<H>());
		}

		return TYPES.get(clazz);
	}


}
