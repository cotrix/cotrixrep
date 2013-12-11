/**
 * 
 */
package org.cotrix.configuration;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import org.cotrix.common.Configuration;
import org.junit.Before;
import org.junit.Test;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestConfigurationReader {
	
	protected static final String EMPTY_CONFIGURATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><cotrix></cotrix>";

	protected ConfigurationReader reader;
	
	@Before
	public void setUp() throws Exception {
		reader = new ConfigurationReader(Collections.<Class<? extends Configuration>>singletonList(MyConfiguration.class).iterator());
	}

	@Test
	public void testEmptyConfiguration() {
		List<Configuration> configurations = reader.readFromStream(new ByteArrayInputStream(EMPTY_CONFIGURATION.getBytes()));
		assertNotNull(configurations);
		assertEquals(configurations.size(), 0);
	}
	
	@Test
	public void testConfigurationRead() {
		
		String xml = TestUtil.getSampleConfigurationXML();
		
		List<Configuration> configurations = reader.readFromStream(new ByteArrayInputStream(xml.getBytes()));
		assertNotNull(configurations);
		assertEquals(configurations.size(), 1);
		
		Configuration readConfiguration = configurations.get(0);
		assertTrue(readConfiguration instanceof MyConfiguration);
		MyConfiguration readMyConfiguration = (MyConfiguration)readConfiguration;
		assertEquals(TestUtil.SAMPLE_CONFIGURATION.firstParameter, readMyConfiguration.firstParameter);
		assertEquals(TestUtil.SAMPLE_CONFIGURATION.secondParameter, readMyConfiguration.secondParameter);
	}
}
