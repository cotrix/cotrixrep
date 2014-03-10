/**
 * 
 */
package org.acme.util;

import java.lang.reflect.Method;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.google.web.bindery.event.shared.binder.EventBinder;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EventBinderTypeListener implements TypeListener {

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		System.out.println("CHECKING "+type.getRawType());
		for (Method method:type.getRawType().getMethods()) {
			if (containsEventBinder(method.getParameterTypes())) {
				System.out.println(type.getRawType()+" CONTAINS EventBinder!!!!");
			}
		}
	}
	
	private boolean containsEventBinder(Class<?>[] types) {
		for (Class<?> type:types) if (type == EventBinder.class) return true;
		return false;
	}

}
