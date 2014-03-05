/**
 * 
 */
package org.acme.util;

import java.lang.reflect.Field;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ModuleAnnotations {

	public static void init(Object o) {
		TestModule module = new TestModule(o);
		Injector injector = Guice.createInjector(module);
		inject(o, injector);
	}
	
	private static void inject(Object o, Injector injector)  {
		Class<?> clazz = o.getClass();
		System.out.println("scanning "+clazz.getName());
		try {
			for (Field field:clazz.getDeclaredFields()) {
				System.out.println("checking field "+field.getName());
				GuiceInject annotation = field.getAnnotation(GuiceInject.class);
				if (annotation!=null) {
					Class<?> type = field.getType();
					field.setAccessible(true);
					Object instance = injector.getInstance(type);
					field.set(o, instance);
				}
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
