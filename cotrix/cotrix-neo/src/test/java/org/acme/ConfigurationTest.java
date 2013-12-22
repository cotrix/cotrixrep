package org.acme;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import javax.inject.Inject;

import org.cotrix.configuration.ConfigurationContext;
import org.cotrix.configuration.Provider;
import org.cotrix.neo.NeoConfiguration;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class ConfigurationTest extends ApplicationTest {

	@Inject
	Provider<NeoConfiguration> provider;
	
	@Inject
	ConfigurationContext context;
	
	@Test
	public void hasDefaultLocation() {
	
		NeoConfiguration config = provider.get();
		
		assertNotNull(config.location());
		assertNotNull(config.properties());
		
	}
	
	@Test(expected=RuntimeException.class)
	public void locationMustBeValid() {
	
		context.bind(new ByteArrayInputStream("<cotrix><neo><location='bad' /></neo></cotrix>".getBytes()));
		
	}
}
