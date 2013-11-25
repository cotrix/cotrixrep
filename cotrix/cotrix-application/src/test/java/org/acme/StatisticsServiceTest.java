package org.acme;

import static org.cotrix.user.Users.*;

import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.cotrix.application.StatisticsService;
import org.cotrix.application.impl.MStatisticsService;
import org.cotrix.common.cdi.Current;
import org.cotrix.repository.CodelistRepository;
import org.cotrix.repository.spi.DefaultCodelistRepository;
import org.cotrix.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class)
public class StatisticsServiceTest {

	@Inject @New(MStatisticsService.class)
	StatisticsService service;
	
	@Inject @New(DefaultCodelistRepository.class)	
	CodelistRepository repository;
	
	@Test
	public void test() {
		System.out.println(service);
		System.out.println(repository);
	}
	
	@Test
	public void test2() {
		System.out.println(service);
		System.out.println(repository);
	}
	
//	@Test
//	public void statistics() {
//		
//		Code c1 = code().name("c1").build();
//		Code c2 = code().name("c2").build();
//		Code c3 = code().name("c3").build();
//		
//		Codelist l1 = codelist().name("test").with(c1,c2).build();
//		Codelist l2 = codelist().name("test2").with(c3).build();
//		
//		repository.add(l1);
//		repository.add(l2);
//		
//		Statistics s = service.statistics();
//		
//		System.out.println(s);
//		
//		assertEquals(2,s.totalCodelists());
//		assertEquals(3,s.totalCodes());
//		assertEquals(predefinedUsers.size(),s.totalUsers());
//		
//	}
//	
//	@Test
//	public void statisticsOnEmptySet() {
//		
//		Statistics s = service.statistics();
//		
//		assertEquals(0, s.totalCodelists());
//		assertEquals(0, s.totalCodes());
//	}
	
	@Produces @Current
	public User user() {
		return cotrix;
	}
	
}
