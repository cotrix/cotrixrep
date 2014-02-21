package org.cotrix.configuration;

import static org.cotrix.common.Constants.*;
import static org.cotrix.configuration.LocationProvider.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cotrix.configuration.utils.CdiProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationLocator {

	private static Logger log = LoggerFactory.getLogger(ConfigurationLocator.class);

	private final Iterable<LocationProvider> providers;

	@Inject
	public ConfigurationLocator(Instance<LocationProvider> providers) {

		this( (Iterable<LocationProvider>) providers);
	}
	
	//test-door
	public ConfigurationLocator(Iterable<LocationProvider> providers) {
		this.providers=providers;
	}

	public InputStream locate() throws IllegalStateException {

		for (LocationProvider provider : providers) {

			String location = provider.location();

			if (location == PASS)
				continue;
			else
				try {
					
					return resolve(location);
					
					
				} catch (RuntimeException e) {
					
					throw new IllegalStateException(" cannot access the configuration @ " + location + " (" + provider.getClass().getCanonicalName() + ")", e);
				}

		}

		return defaultConfiguration();

	}

	// helpers

	private InputStream resolve(String location) {

		if (location == null)
			throw new RuntimeException("location is undefined");

		File file = new File(location);

		if (!file.exists() || !file.canRead() || file.isDirectory())
			throw new RuntimeException("configuration @ " + file + " does not exist, cannot be read, or is a directory");

		try {

			InputStream stream = new FileInputStream(file);

			log.info("using configuration @ {}", file);

			return stream;

		} catch (Exception e) {

			throw new RuntimeException("cannot read configuration @ " + file, e);
		}

	}

	private InputStream defaultConfiguration() {

		log.info("no configuration provided: using defaults");

		InputStream stream = CdiProducer.class.getResourceAsStream(DEFAULT_CONFIGURATION_PATH);

		if (stream == null)
			throw new AssertionError("no default configuration: invalid distribution?");

		return stream;
	}

}
