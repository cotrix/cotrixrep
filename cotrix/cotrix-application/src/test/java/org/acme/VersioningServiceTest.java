package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;

import javax.inject.Inject;

import org.cotrix.application.VersioningService;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.codelist.CodelistRepository;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class VersioningServiceTest {

	@Inject
	VersioningService service;
	
	@Inject
	CodelistRepository repository;
	
	@Test
	public void versionCodelist() {
		
		Codelist codelist = codelist().name("test").build();
		
		String version = codelist.version();
		
		assertFalse(version.equals("2014"));
		
		Codelist versioned = service.bump(codelist).to("2014");
		
		assertEquals("2014", versioned.version());
		
	}
	
	@Test
	public void fetchesVersionsAndPersistsUseCase() {
		
		Codelist codelist = codelist().name("test").version("2013").build();
		
		repository.add(codelist);
		
		Codelist fetched = repository.lookup(codelist.id());
		
		Codelist versioned = service.bump(fetched).to("2014");
		
		repository.add(versioned);
		
		assertNotNull(repository.lookup(versioned.id()));
		
		repository.remove(versioned.id());
		repository.remove(codelist.id());
		
	}
}
