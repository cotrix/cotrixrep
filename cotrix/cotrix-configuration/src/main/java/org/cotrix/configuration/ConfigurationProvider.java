package org.cotrix.configuration;

import javax.enterprise.inject.Vetoed;

import org.cotrix.common.Configuration;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@Vetoed
public class ConfigurationProvider<T extends Configuration> {

	protected T configuration;

	/**
	 * @param configuration
	 */
	protected ConfigurationProvider(T configuration) {
		this.configuration = configuration;
	}

	/**
	 * @return the configuration
	 */
	public T getConfiguration() {
		return configuration;
	}
}
