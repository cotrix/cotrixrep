/**
 * 
 */
package org.acme.util;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureBus;
import org.cotrix.web.server.MainServiceImpl;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.AbstractModule;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.GenericEvent;
import com.google.web.bindery.event.shared.binder.impl.GenericEventHandler;
import com.google.web.bindery.event.shared.binder.impl.GenericEventType;
import com.googlecode.jeeunit.cdi.BeanManagerLookup;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestModule extends AbstractModule {

	private Class<?>[][] serviceBindings = {
			{MainServiceAsync.class, MainServiceImpl.class}
	};


	private List<Binding> bindings = new ArrayList<>();

	private EventBus cotrixBus = new SimpleEventBus();
	private EventBus featuresBus = new SimpleEventBus();

	public TestModule(Object o) {
		scan(o);
	}

	private void scan(Object o)  {
		Class<?> clazz = o.getClass();
		try {
			for (Field field:clazz.getDeclaredFields()) {
				Provide annotation = field.getAnnotation(Provide.class);
				if (annotation!=null) {
					Class<?> type = field.getType();
					field.setAccessible(true);
					Object value = field.get(o);
					bindings.add(new Binding(type, value));
				}
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the cotrixBus
	 */
	public EventBus getCotrixBus() {
		return cotrixBus;
	}

	/**
	 * @param cotrixBus the cotrixBus to set
	 */
	public void setCotrixBus(EventBus cotrixBus) {
		this.cotrixBus = cotrixBus;
	}

	/**
	 * @return the featuresBus
	 */
	public EventBus getFeaturesBus() {
		return featuresBus;
	}

	/**
	 * @param featuresBus the featuresBus to set
	 */
	public void setFeaturesBus(EventBus featuresBus) {
		this.featuresBus = featuresBus;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {

		findEventBinders();

		requestStaticInjection(FeatureBinder.class);

		for (Binding binding:bindings) bind((Class<Object>)binding.type).toInstance(binding.value);

		bind(EventBus.class).annotatedWith(CotrixBus.class).toInstance(cotrixBus);
		bind(EventBus.class).annotatedWith(FeatureBus.class).toInstance(featuresBus);

		for (Class<?>[] serviceBinding:serviceBindings) {
			Class<?> asyncServiceType = serviceBinding[0];
			Class<?> serviceType = serviceBinding[1];
			Object mock = mock(asyncServiceType, new ServiceInterceptor(serviceType));
			bind((Class<Object>)asyncServiceType).toInstance(mock);
		}
	}

	private static Reflections reflections = new Reflections("org.cotrix.web");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void findEventBinders() {
		Set<Class<? extends EventBinder>> binders = reflections.getSubTypesOf(EventBinder.class);
		for (Class<? extends EventBinder> binder:binders) bindBinder(binder);
	}

	@SuppressWarnings("unchecked")
	private <B,T extends EventBinder<B>> void bindBinder(Class<T> binder) {
		System.out.println("binding "+binder.getName());

		try {

			T myBinder = mock(binder);

			ParameterizedType parameterizedType = (ParameterizedType) binder.getGenericInterfaces()[0];
			Class<?> bindedClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
			System.out.println("Binder for :"+bindedClass);

			Set<Method> eventHandlerMethods = ReflectionUtils.getAllMethods(bindedClass, ReflectionUtils.withParametersAssignableTo(GenericEvent.class));

			when(myBinder.bindEventHandlers((B)Mockito.any(), Mockito.any(EventBus.class))).thenAnswer(new EventBinderHelper<B>(eventHandlerMethods));

			bind(binder).toInstance(myBinder);
		} catch(Throwable throwable){
			throwable.printStackTrace();
		}
	}

	private class EventBinderHelper<T> implements Answer<HandlerRegistration> {
		
		Set<Method> eventHandlerMethods;
		
		/**
		 * @param eventHandlerMethods
		 */
		private EventBinderHelper(Set<Method> eventHandlerMethods) {
			this.eventHandlerMethods = eventHandlerMethods;
		}

		@SuppressWarnings("unchecked")
		public HandlerRegistration answer(InvocationOnMock invocation) {
			Object[] args = invocation.getArguments();

			final List<HandlerRegistration> registrations = bind((T)args[0], (EventBus)args[1]);
		    return new HandlerRegistration() {
		      @Override
		      public void removeHandler() {
		        for (HandlerRegistration registration : registrations) {
		          registration.removeHandler();
		        }
		        registrations.clear();
		      }
		    };
		}

		@SuppressWarnings("unchecked")
		protected List<HandlerRegistration> bind(final T instance, EventBus eventBus) {
			List<HandlerRegistration> registrations = new ArrayList<>();
			for (final Method eventHandlerMethod:eventHandlerMethods) {
				eventHandlerMethod.setAccessible(true);
				Class<? extends GenericEvent> eventType = (Class<? extends GenericEvent>) eventHandlerMethod.getParameterTypes()[0];

				GenericEventHandler eventHandler = new GenericEventHandler() {

					@Override
					public void handleEvent(GenericEvent event) {
						try {
							eventHandlerMethod.invoke(instance, event);
						} catch (IllegalAccessException
								| IllegalArgumentException
								| InvocationTargetException e) {
							throw new RuntimeException(e);
						}
					}
				};

				bind(eventBus, registrations, eventType, eventHandler);

			}
			return registrations;
		}


		protected final <U extends GenericEvent> void bind(
				EventBus eventBus,
				List<HandlerRegistration> registrations,
				Class<U> type,
				GenericEventHandler handler) {
			registrations.add(eventBus.addHandler(GenericEventType.getTypeOf(type), handler));
		}
	}

	private class Binding {
		Class<?> type;
		Object value;
		/**
		 * @param type
		 * @param value
		 */
		private Binding(Class<?> type, Object value) {
			this.type = type;
			this.value = value;
		}
	}

	private class ServiceInterceptor implements Answer<Object> {

		Class<?> serviceType;
		Object service;

		private ServiceInterceptor(Class<?> serviceType) {
			this.serviceType = serviceType;
		}

		@Override
		public Object answer(InvocationOnMock invocation) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException {

			instantiateService();

			Class<?>[] parTypes = Arrays.copyOfRange(invocation.getMethod().getParameterTypes(), 0, invocation.getMethod().getParameterTypes().length-1);
			Method method = service.getClass().getMethod(invocation.getMethod().getName(), parTypes);

			Object[] args = Arrays.copyOfRange(invocation.getArguments(), 0, invocation.getArguments().length-1);

			@SuppressWarnings("unchecked")
			AsyncCallback<Object> callback = (AsyncCallback<Object>) invocation.getArguments()[invocation.getArguments().length-1];

			try {
				Object result = method.invoke(service, args);
				callback.onSuccess(result);
			} catch(InvocationTargetException e) {
				callback.onFailure(e.getTargetException());
			}
			return null;
		}

		protected void instantiateService() {
			if (service == null) service = lookup(serviceType);
		}

		@SuppressWarnings("unchecked")
		private <T> T lookup(Class<T> clazz, BeanManager bm) {
			Iterator<Bean< ? >> iter = bm.getBeans(clazz).iterator();
			if (!iter.hasNext()) {
				throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type " + clazz.getName());
			}
			Bean<T> bean = (Bean<T>) iter.next();
			CreationalContext<T> ctx = bm.createCreationalContext(bean);
			T dao = (T) bm.getReference(bean, clazz, ctx);
			return dao;
		}

		private <T> T lookup(Class<T> clazz) {
			BeanManager bm = BeanManagerLookup.getBeanManager();
			return lookup(clazz, bm);
		}
	}

}
