/**
 * 
 */
package org.cotrix.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.cotrix.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class ContextStreamProvider implements ConfigurationStreamProvider {

	protected Logger logger = LoggerFactory.getLogger(ContextStreamProvider.class);

	@Override
	public InputStream getStream() {
		try {
			Context context = new InitialContext();
			Context envCtx = (Context) context.lookup("java:comp/env");
			String configFileProperty = (String)envCtx.lookup(Constants.CONFIGURATION_FILE_PROPERTY_NAME);
			logger.trace("property {} = {}", Constants.CONFIGURATION_FILE_PROPERTY_NAME, configFileProperty);
			if (configFileProperty!=null) {
				File configFile = new File(configFileProperty);
				if (!configFile.exists()) {
					logger.error("The specified configuration file {} don't exists");
					throw new IllegalArgumentException("Configuration file "+configFileProperty+" not found");
				} else {
					try {
						return new FileInputStream(configFile);
					} catch (FileNotFoundException e) {
						throw new IllegalArgumentException("Failed opening file "+configFile, e);
					}
				}
			}
		} catch(Exception e) {
			logger.error("Failed retrieving property from context", e);
		}
		return null;
	}

}
