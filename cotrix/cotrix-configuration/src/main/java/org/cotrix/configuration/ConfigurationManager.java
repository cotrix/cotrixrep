/**
 * 
 */
package org.cotrix.configuration;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.Configuration;
import org.cotrix.common.cdi.ApplicationEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Singleton
public class ConfigurationManager {
	
	protected static final String DEFAULT_CONFIGURATION_RESOURCE_NAME = "/default-configuration.xml";
	
	protected Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

	@Inject
	protected Instance<Configuration> configurationSamples; 
	
	@Inject
	protected Instance<ConfigurationStreamProvider> streamProviders;
	
	
	protected Map<Class<? extends Configuration>, Configuration> configurations;
	
	@PostConstruct
	@Inject
	protected void init() {
		
		logger.trace("init ConfigurationManager");
		
		Iterator<Configuration> sampleIterator = configurationSamples.iterator();
		List<Class<? extends Configuration>> configurationClasses = new ArrayList<Class<? extends Configuration>>();
		while(sampleIterator.hasNext()) {
			Configuration sample = sampleIterator.next();
			logger.trace("configuration class "+sample.getClass());
			configurationClasses.add(sample.getClass());
		}
		
		ConfigurationReader configurationReader = new ConfigurationReader(configurationClasses.iterator());
		InputStream stream = getConfigurationStream(); 
		
		List<Configuration> confs = configurationReader.readFromStream(stream);
		configurations = new HashMap<Class<? extends Configuration>, Configuration>();
		for (Configuration configuration:confs) {
			Class<? extends Configuration> key = configuration.getClass();
			configurations.put(key, configuration);
		}
	}
	
	protected InputStream getConfigurationStream() {
		Iterator<ConfigurationStreamProvider> providers = streamProviders.iterator();
		while(providers.hasNext()) {
			InputStream stream = providers.next().getStream();
			if (stream != null) return stream;
		}
		return ConfigurationManager.class.getResourceAsStream(DEFAULT_CONFIGURATION_RESOURCE_NAME);
	}

	@SuppressWarnings("unchecked")
	@Produces
	public <T extends Configuration> ConfigurationProvider<T> getConfiguration(InjectionPoint ip) {
		ParameterizedType type = (ParameterizedType) ip.getType();  
        Type[] typeArgs = type.getActualTypeArguments();  
        Class<T> configClass = (Class<T>) typeArgs[0];  
        Configuration configuration = configurations.get(configClass);
		return new ConfigurationProvider<T>((T) configuration);		
	}
	
	public static class ConfigurationManagerInjector {
		
		protected Logger logger = LoggerFactory.getLogger(ConfigurationManagerInjector.class);

		void configure(@Observes ApplicationEvents.Startup event, ConfigurationManager manager) {
			logger.trace("configuration mangement is initialized");
		}
	}
}
