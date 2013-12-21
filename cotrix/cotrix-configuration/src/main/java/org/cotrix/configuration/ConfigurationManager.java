/**
 * 
 */
package org.cotrix.configuration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cotrix.common.ConfigurationBean;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 * 
 */
@Singleton
public class ConfigurationManager {

	private final Map<Class<?>, ConfigurationBean> configurations = new HashMap<Class<?>, ConfigurationBean>();

	@Inject
	public ConfigurationManager(ConfigurationLocator locator, ConfigurationContext context) {

		this(locator.locate(),context);

	}
	
	public ConfigurationManager(InputStream stream, ConfigurationContext context) {

		List<ConfigurationBean> list = context.bind(stream);

		for (ConfigurationBean configuration : list)
			configurations.put(configuration.getClass(), configuration);

	}
	
	public <T extends ConfigurationBean> T get(Class<T> type) throws IllegalStateException {
		
		@SuppressWarnings("unchecked")
		//safe by construction
		T bean = (T) configurations.get(type);

		// stop at startup if configuration is required but not available
		if (bean == null)
			throw new IllegalStateException("no configuration for " + type);
		
		return bean;
	}

}
