package org.acme;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.dsl.Users.*;
import static org.junit.Assert.*;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.application.StatisticsService;
import org.cotrix.application.StatisticsService.Statistics;
import org.cotrix.common.cdi.ApplicationEvents.ApplicationEvent;
import org.cotrix.common.cdi.ApplicationEvents.Shutdown;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.user.User;
import org.cotrix.repository.codelist.CodelistRepository;
import org.cotrix.repository.user.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class StatisticsServiceTest {

	@Inject
	StatisticsService service;
	
	@Inject	
	CodelistRepository repository;
	
	@Inject
	UserRepository uRepository;
	
	@Inject
	Event<ApplicationEvent> events;

	@Test
	public void statistics() {
		
		Code c1 = code().name("c1").build();
		Code c2 = code().name("c2").build();
		Code c3 = code().name("c3").build();
		
		Codelist l1 = codelist().name("test").with(c1,c2).build();
		Codelist l2 = codelist().name("test2").with(c3).build();
		
		repository.add(l1);
		repository.add(l2);
		
		User joe = user().name("joe").email("joe@me.com").build();
		
		uRepository.add(joe);
		
		
		Statistics s = service.statistics();
		
		assertEquals(2,s.totalCodelists());
		assertEquals(3,s.totalCodes());		
		assertEquals(1,s.totalUsers());		
	}
	
	@Test
	public void statisticsOnEmptySet() {
		
		Statistics s = service.statistics();
		
		assertEquals(0, s.totalCodelists());
		assertEquals(0, s.totalCodes());
	}
	
	//sadly, we cannot control injection on a per-test basis, so we need to cleanup after each test
	//we do it with end-of-app events
	@After
	public void shutdown() {
		events.fire(Shutdown.INSTANCE);
	}

	
}
