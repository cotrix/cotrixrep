/**
 * 
 */
package org.acme.util;

import java.lang.reflect.Field;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.MembersInjector;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import static org.mockito.Mockito.*;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestGuice {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyModule module = new MyModule();
		Injector injector = Guice.createInjector(module);
		injector.getInstance(MyContainer.class);
	}
	
	public static class MyModule extends AbstractModule {

		@Override
		protected void configure() {
			//bind(MyInterface.class); //.to(MyImpl.class);
			bind(MyInterface.class).toInstance(mock(MyInterface.class));
			bindListener(Matchers.any(), new MyListener());
			bind(new TypeLiteral<MyInterface<? extends Object>>() {
			}).toInstance(mock(MyInterface.class));
		}
	}
	
	public static class MyContainer {
		
		@Inject
		String test;
		
		@Inject
		MyInterface<String> myInterface;
	}
	
	public static interface MyInterface<T> {}
	public static class MyImpl<T> implements MyInterface<T> {}
	
	public static class MyListener implements TypeListener {

		@Override
		public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
			System.out.println("RAW TYPE: "+type.getRawType());
			System.out.println(" TYPE: "+type.getType());
			for (Field field: type.getRawType().getDeclaredFields())
			if (MyInterface.class.isAssignableFrom(field.getType())) {
				System.out.println("field: "+field.getName()+" type: "+field.getType());
				encounter.register(new MyInjector<>(field));
			}
		}
		
	}
	
	public static class MyInjector<T> implements MembersInjector<T> {
		
		private final Field field;
		
		

		/**
		 * @param field
		 */
		private MyInjector(Field field) {
			this.field = field;
		}



		@Override
		public void injectMembers(T instance) {
			try {
				field.set(instance, new MyImpl<>());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
