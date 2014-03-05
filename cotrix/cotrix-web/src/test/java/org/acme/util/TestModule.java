/**
 * 
 */
package org.acme.util;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureBus;
import org.cotrix.web.server.MainServiceImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.AbstractModule;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
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
		System.out.println("scanning "+clazz.getName());
		try {
			for (Field field:clazz.getDeclaredFields()) {
				System.out.println("checking field "+field.getName());
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
		requestStaticInjection(FeatureBinder.class);

		for (Binding binding:bindings) bind((Class<Object>)binding.type).toInstance(binding.value);

		bind(EventBus.class).annotatedWith(CotrixBus.class).toInstance(cotrixBus);
		bind(EventBus.class).annotatedWith(FeatureBus.class).toInstance(featuresBus);

		for (Class<?>[] serviceBinding:serviceBindings) {
			System.out.println("mocking "+serviceBinding);
			Class<?> asyncServiceType = serviceBinding[0];
			Class<?> serviceType = serviceBinding[1];
			Object mock = mock(asyncServiceType, new ServiceInterceptor(serviceType));
			bind((Class<Object>)asyncServiceType).toInstance(mock);
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
		public Object answer(InvocationOnMock invocation) throws NoSuchMethodException, SecurityException {

			System.out.println("intercepting "+invocation.getMethod().getName());

			instantiateService();

			Class<?>[] parTypes = Arrays.copyOfRange(invocation.getMethod().getParameterTypes(), 0, invocation.getMethod().getParameterTypes().length-1);
			Method method = service.getClass().getMethod(invocation.getMethod().getName(), parTypes);

			Object[] args = Arrays.copyOfRange(invocation.getArguments(), 0, invocation.getArguments().length-1);

			@SuppressWarnings("unchecked")
			AsyncCallback<Object> callback = (AsyncCallback<Object>) invocation.getArguments()[invocation.getArguments().length-1];

			try {
				Object result = method.invoke(service, args);
				callback.onSuccess(result);
			} catch(Exception e) {
				callback.onFailure(e);
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
