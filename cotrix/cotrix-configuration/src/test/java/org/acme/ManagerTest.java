/**
 * 
 */
package org.acme;

import static org.cotrix.common.Constants.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.inject.Inject;

import org.cotrix.configuration.Provider;
import org.cotrix.test.ApplicationTest;
import org.junit.BeforeClass;
import org.junit.Test;


public class ManagerTest extends ApplicationTest {
	
	@Inject
	protected Provider<SampleConfig> configuration;
	
	@BeforeClass
	public static void setup() throws IOException {
		
		System.setProperty(CONFIGURATION_PROPERTY, SampleConfig.asFile().getAbsolutePath());

	}
	
	@Test
	public void produceConfiguration() throws IOException {
		
		assertNotNull(configuration);
		
		assertNotNull(configuration.get());
		
		assertTrue(configuration.get() instanceof SampleConfig);
		
	}
}
