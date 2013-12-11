package org.cotrix.configuration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.cotrix.common.Constants;
import org.junit.Before;
import org.junit.Test;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestSystemPropertyStreamProvider {
	
	protected SystemPropertyStreamProvider provider;

	@Before
	public void setUp() throws Exception {
		provider = new SystemPropertyStreamProvider();
	}

	@Test
	public void testMissingProperty() {
		System.getProperties().remove(Constants.CONFIGURATION_FILE_PROPERTY_NAME);
		InputStream stream = provider.getStream();
		assertNull(stream);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMissingFile() throws IOException {
		System.getProperties().put(Constants.CONFIGURATION_FILE_PROPERTY_NAME, "");
		provider.getStream();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUnreadableFile() throws IOException {
		File tmp = File.createTempFile("TestSystemPropertyStreamProvider", "tmp");
		System.getProperties().put(Constants.CONFIGURATION_FILE_PROPERTY_NAME, tmp.getAbsolutePath());
		tmp.setReadable(false);
		
		tmp.deleteOnExit();
		
		provider.getStream();
	}
	
	@Test
	public void test() throws IOException {
		
		File tmp = File.createTempFile("TestSystemPropertyStreamProvider", "tmp");
		System.getProperties().put(Constants.CONFIGURATION_FILE_PROPERTY_NAME, tmp.getAbsolutePath());
		tmp.deleteOnExit();
		
		InputStream stream = provider.getStream();
		assertNotNull(stream);
		stream.close();
	}

}
