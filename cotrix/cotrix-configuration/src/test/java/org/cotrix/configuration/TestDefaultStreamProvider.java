package org.cotrix.configuration;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class TestDefaultStreamProvider {
	
	protected DefaultStreamProvider provider;

	@Before
	public void setUp() throws Exception {
		provider = new DefaultStreamProvider();
	}
	
	@Test
	public void test() throws IOException {

		InputStream stream = provider.getStream();
		assertNotNull(stream);
		stream.close();
	}

}
