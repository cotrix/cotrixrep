package org.cotrix;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.domain.Codelist;
import org.cotrix.io.ImportService;
import org.cotrix.io.ingest.Outcome;
import org.cotrix.io.sdmx.SdmxImportDirectives;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.virtual.sr.RepositoryPlugin;
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
	public void discoverRemoteSdmxCodelist() {
		
		service.discoverRemoteCodelists();
		
		for (Asset codelist : service.remoteCodelists())
			System.out.println(codelist);
	}
	
	@Test
	public void discoverAndImportRemoteSdmxCodelist() {
		
		service.discoverRemoteCodelists();
		
		Asset remoteCodelist  = service.remoteCodelists().iterator().next();
		
		Outcome<Codelist> outcome = service.importCodelist(remoteCodelist.id(), SdmxImportDirectives.DEFAULT); 
		
		System.out.println(outcome.result());
	}
	
	@Test
	public void discoverAndImportRemoteSdmxCodelistFromSR() {
		
		service.discoverRemoteCodelists();
		
		Asset srCodelist = null;
		for (Asset remoteCodelist : service.remoteCodelists())
			if (remoteCodelist.service().name().equals(RepositoryPlugin.name)) {
				srCodelist=remoteCodelist;
				break;
			}
		
		if (srCodelist!=null) {
			Outcome<Codelist> outcome = service.importCodelist(srCodelist.id(), SdmxImportDirectives.DEFAULT); 
			System.out.println(outcome.result());
		}
	}
	
	
}