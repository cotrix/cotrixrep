/**
 * 
 */
package org.acme.util;

import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.acme.util.TestModule.Binding;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.GenericEvent;
import com.google.web.bindery.event.shared.binder.impl.GenericEventHandler;
import com.google.web.bindery.event.shared.binder.impl.GenericEventType;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class EventBinderBindingsProvider implements BindingsProvider {

	private static Reflections reflections = new Reflections("org.cotrix.web");

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Binding> getBindings() {

		Set<Class<? extends EventBinder>> binders = reflections.getSubTypesOf(EventBinder.class);
		if (binders.isEmpty()) return Collections.emptyList();
		List<Binding> bindings = new ArrayList<>();
		for (Class<? extends EventBinder> binder:binders) {
			Binding binding = generateBinding(binder);
			bindings.add(binding);
		}
		return bindings;
	}

	@SuppressWarnings("unchecked")
	private <B,T extends EventBinder<B>> Binding generateBinding(Class<T> binder) {

		T mockedBinder = mock(binder);

		ParameterizedType parameterizedType = (ParameterizedType) binder.getGenericInterfaces()[0];
		Class<?> bindedClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

		Set<Method> eventHandlerMethods = ReflectionUtils.getAllMethods(bindedClass, ReflectionUtils.withParametersAssignableTo(GenericEvent.class));

		when(mockedBinder.bindEventHandlers((B)Mockito.any(), Mockito.any(EventBus.class))).thenAnswer(new EventBinderHelper<B>(eventHandlerMethods));

		return new Binding(binder, mockedBinder);

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

}
