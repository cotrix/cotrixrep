/**
 * 
 */
package org.cotrix.configuration;

import static org.junit.Assert.*;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.cotrix.common.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
@RunWith(JeeunitRunner.class)
public class TestConfigurationManager {
	
	@Inject
	protected ConfigurationManagerSetup setup;
	
	protected ConfigurationProvider<MyConfiguration> provider;
	
	@PostConstruct
	@Inject
	protected void init(ConfigurationProvider<MyConfiguration> provider) {
		this.provider = provider;
	}

	@Test
	public void test() {
		assertNotNull(setup);
		assertNotNull(setup.manager);
		
		assertNotNull(provider);
		
		MyConfiguration configuration = provider.getConfiguration();
		assertNotNull(configuration);
		
		assertEquals(TestUtil.SAMPLE_CONFIGURATION.firstParameter, configuration.firstParameter);
		assertEquals(TestUtil.SAMPLE_CONFIGURATION.secondParameter, configuration.secondParameter);
	}
	
	static class ConfigurationManagerSetup {
		
		ConfigurationManager manager;
		
		public ConfigurationManagerSetup() {
			try {
			File tmp = TestUtil.getSampleConfigurationTmpFile();
			System.getProperties().put(Constants.CONFIGURATION_FILE_PROPERTY_NAME, tmp.getAbsolutePath());
			} catch(Exception e) {}
		}
		
		@PostConstruct
		@Inject
		protected void init(ConfigurationManager manager) {
			this.manager = manager;
		}
	}
}
