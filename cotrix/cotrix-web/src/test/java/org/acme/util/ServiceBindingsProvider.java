/**
 * 
 */
package org.acme.util;

import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.acme.util.TestModule.Binding;
import org.cotrix.web.client.MainServiceAsync;
import org.cotrix.web.server.MainServiceImpl;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.jeeunit.cdi.BeanManagerLookup;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ServiceBindingsProvider implements BindingsProvider {
	
	private Class<?>[][] serviceBindings = {
			{MainServiceAsync.class, MainServiceImpl.class}
	};

	@Override
	public List<Binding> getBindings() {
		List<Binding> bindings = new ArrayList<>();
		for (Class<?>[] serviceBinding:serviceBindings) {
			Class<?> asyncServiceType = serviceBinding[0];
			Class<?> serviceType = serviceBinding[1];
			Object mock = mock(asyncServiceType, new ServiceInterceptor(serviceType));
			bindings.add(new Binding(asyncServiceType, mock));
		}
		return bindings;
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
