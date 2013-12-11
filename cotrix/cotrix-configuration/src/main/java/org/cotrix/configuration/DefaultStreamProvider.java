/**
 * 
 */
package org.cotrix.configuration;

import java.io.InputStream;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class DefaultStreamProvider implements ConfigurationStreamProvider {
	
	protected static final String DEFAULT_CONFIGURATION_RESOURCE_NAME = "/default-configuration.xml";

	@Override
	public InputStream getStream() {
		return DefaultStreamProvider.class.getResourceAsStream(DEFAULT_CONFIGURATION_RESOURCE_NAME);
	}

}
