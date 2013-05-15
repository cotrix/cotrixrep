package org.cotrix;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.domain.Codelist;
import org.cotrix.io.ImportService;
import org.cotrix.io.ingest.Outcome;
import org.cotrix.io.sdmx.SdmxImportDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtualrepository.Asset;

import com.googlecode.jeeunit.JeeunitRunner;


@RunWith(JeeunitRunner.class)
public class ImportIntegrationTests {

	@Inject
	ImportService service;
	
	@Test
	public void servicesAreInjected() throws Exception {
			
		assertNotNull(service);
		
	}
	
	@Test
	public void discoverAndImportRemoteSdmxCodelist() {
		
		service.discoverRemoteCodelists();
		
		Asset remoteCodelist  = service.remoteCodelists().iterator().next();
		
		Outcome<Codelist> outcome = service.importCodelist(remoteCodelist.id(), SdmxImportDirectives.DEFAULT); 
		
		System.out.println(outcome.result());
	}
	
	
}