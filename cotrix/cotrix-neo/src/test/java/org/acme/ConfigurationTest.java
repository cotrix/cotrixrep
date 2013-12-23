package org.acme;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import javax.inject.Inject;

import org.cotrix.common.cdi.Current;
import org.cotrix.configuration.ConfigurationContext;
import org.cotrix.neo.NeoConfiguration;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class ConfigurationTest extends ApplicationTest {

	@Inject @Current
	NeoConfiguration configuration;
	
	@Inject
	ConfigurationContext context;
	
	@Test
	public void hasDefaultLocation() {
	
		assertNotNull(configuration.location());
		assertNotNull(configuration.properties());
		
	}
	
	@Test(expected=RuntimeException.class)
	public void locationMustBeValid() {
	
		context.bind(new ByteArrayInputStream("<cotrix><neo><location='bad' /></neo></cotrix>".getBytes()));
		
	}
	
}
