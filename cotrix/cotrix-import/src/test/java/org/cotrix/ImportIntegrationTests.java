package org.cotrix;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.importservice.ImportService;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class ImportIntegrationTests {

	@Inject
	ImportService service;
	
	@Test
	public void servicesAreInjected() throws Exception {
			
		assertNotNull(service);
		
	}
	
}