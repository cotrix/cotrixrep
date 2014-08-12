package org.acme;

import static org.cotrix.domain.dsl.Entities.*;
import static org.junit.Assert.*;

import javax.inject.Inject;

import org.cotrix.application.StatisticsService;
import org.cotrix.application.StatisticsService.Statistics;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.UserRepository;
import org.cotrix.test.ApplicationTest;
import org.junit.Test;

public class StatisticsServiceTest extends ApplicationTest {

	@Inject
	StatisticsService service;
	
	@Inject	
	CodelistRepository repository;
	
	@Inject
	UserRepository uRepository;
	
	@Test
	public void statistics() {
		
		Code c1 = code().name("c1").build();
		Code c2 = code().name("c2").build();
		Code c3 = code().name("c3").build();
		
		Codelist l1 = codelist().name("test").with(c1,c2).build();
		Codelist l2 = codelist().name("test2").with(c3).build();
		
		repository.add(l1);
		repository.add(l2);
		
		Statistics s = service.statistics();
		
		assertEquals(2,s.totalCodelists());
		assertEquals(3,s.totalCodes());		
	}
	
	@Test
	public void statisticsOnEmptySet() {
		
		Statistics s = service.statistics();
		
		assertEquals(0, s.totalCodelists());
		assertEquals(0, s.totalCodes());
	}
}
