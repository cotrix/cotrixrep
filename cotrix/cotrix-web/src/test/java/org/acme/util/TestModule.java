/**
 * 
 */
package org.acme.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureBus;

import com.google.inject.AbstractModule;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestModule extends AbstractModule {

	private BindingsProvider[] providers = {new EventBinderBindingsProvider(), new ServiceBindingsProvider()};

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

		for (BindingsProvider provider:providers) {
			bindings.addAll(provider.getBindings());
		}

		requestStaticInjection(FeatureBinder.class);

		for (Binding binding:bindings) bind((Class<Object>)binding.type).toInstance(binding.value);

		bind(EventBus.class).annotatedWith(CotrixBus.class).toInstance(cotrixBus);
		bind(EventBus.class).annotatedWith(FeatureBus.class).toInstance(featuresBus);
	}

	public static class Binding {
		Class<?> type;
		Object value;
		/**
		 * @param type
		 * @param value
		 */
		public Binding(Class<?> type, Object value) {
			this.type = type;
			this.value = value;
		}
	}
}
