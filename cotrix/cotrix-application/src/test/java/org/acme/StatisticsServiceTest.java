package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.domain.dsl.Codes.*;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.application.StatisticsService;
import org.cotrix.application.StatisticsService.Statistics;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.user.PredefinedUsers;
import org.cotrix.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class StatisticsServiceTest {

	@Inject
	StatisticsService service;
	
	@Inject
	CodelistRepository repository;
	
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
		
		System.out.println(s);
		
		assertEquals(2,s.totalCodelists());
		assertEquals(3,s.totalCodes());
		assertEquals(PredefinedUsers.values.size(),s.totalUsers());
		
	}
	
	
	@Produces @Current
	public User user() {
		return PredefinedUsers.guest;
	}
	
}

