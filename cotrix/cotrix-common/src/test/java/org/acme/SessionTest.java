package org.acme;

import static org.junit.Assert.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.cotrix.common.cdi.Session;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.jglue.cdiunit.DummyHttpRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiRunner.class)
public class SessionTest {

	@Inject
	Service service;
	
	
	@Inject
	ContextController contextController; 

	
	@Ignore
	@Test
	public void sessionScopeJustWorks() {

		contextController.openSession(new DummyHttpRequest());

		System.out.println("touching service");
		
		service.session().data().put("one",1);
		
		contextController.closeSession();
		
		contextController.openSession(new DummyHttpRequest()); 

		assertFalse(service.session().data().containsKey("one"));
		
		contextController.closeSession();
	}
	
	@ApplicationScoped
	static class Service {
		
		@Inject
		Session session;
		
		Session session() {
			return session;
		}
		
		@PostConstruct
		public void init() {
			System.out.println(session.getClass());
		}
	}
}
