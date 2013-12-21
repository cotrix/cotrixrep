/**
 * 
 */
package org.acme;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import org.cotrix.common.ConfigurationBean;
import org.cotrix.configuration.ConfigurationContext;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;


public class ContextTest extends ApplicationTest {
	
	@Inject
	ConfigurationContext context;
	
	@Test
	public void parseEmptyConfig() {

		List<ConfigurationBean> beans = context.bind(streamWith("<cotrix/>"));
		
		assertEquals(beans.size(), 0);
	}

	@Test
	public void parseUnboundBean() {

		List<ConfigurationBean> beans = context.bind(streamWith("<cotrix><unbound/></cotrix>"));
		
		assertEquals(beans.size(),0);
	}
	
	@Test
	public void parseBoundBean() {

				
		List<ConfigurationBean> beans = context.bind(streamWith(SampleConfig.instance));
		
		assertEquals(beans.size(), 1);
		assertTrue(beans.get(0) instanceof SampleConfig);
	}
	
	
	//helper
	private InputStream streamWith(String s) {
		
		return new ByteArrayInputStream(s.getBytes());
	}
	

}
