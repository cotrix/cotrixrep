/**
 * 
 */
package org.acme.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.cotrix.web.common.client.event.CotrixBus;
import org.cotrix.web.common.client.feature.FeatureBinder;
import org.cotrix.web.common.client.feature.FeatureBus;

import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
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
					Class<?> type = annotation.realType()!=void.class?annotation.realType():field.getType();
					field.setAccessible(true);
					Object value = field.get(o);
					Class<? extends Annotation> bindingAnnotation = getBindingAnnotation(field.getAnnotations());
					bindings.add(new Binding(type, value, bindingAnnotation));
				}
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Class<? extends Annotation> getBindingAnnotation(Annotation[] annotations) {
		for (Annotation annotation:annotations) {
			if (annotation.annotationType().isAnnotationPresent(BindingAnnotation.class)) return annotation.annotationType();
		}
		return null;
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

		configureDefaultBindings();

		for (Binding binding:bindings) {
			if (binding.annotation!=null) bind((Class<Object>)binding.type).annotatedWith(binding.annotation).toInstance(binding.value);
			else bind((Class<Object>)binding.type).toInstance(binding.value);
		}
	}
	
	private void configureDefaultBindings() {
		requestStaticInjection(FeatureBinder.class);
		if (!existBinding(EventBus.class, CotrixBus.class)) bind(EventBus.class).annotatedWith(CotrixBus.class).toInstance(cotrixBus);
		if (!existBinding(EventBus.class, FeatureBus.class)) bind(EventBus.class).annotatedWith(FeatureBus.class).toInstance(featuresBus);
	}
	
	private <T> boolean existBinding(Class<T> type, Class<? extends Annotation> annotation) {
		for (Binding binding:bindings) if (binding.type == type && binding.annotation == annotation) return true;
		return false;
	}

	public static class Binding {
		Class<?> type;
		Object value;
		Class<? extends Annotation> annotation;

		public Binding(Class<?> type, Object value) {
			this.type = type;
			this.value = value;
		}

		public Binding(Class<?> type, Object value,
				Class<? extends Annotation> annotation) {
			this.type = type;
			this.value = value;
			this.annotation = annotation;
		}
	}
}
