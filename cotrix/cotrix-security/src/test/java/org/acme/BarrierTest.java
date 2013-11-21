package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.user.Users.*;
import static org.mockito.Mockito.*;

import javax.inject.Inject;
import javax.servlet.FilterChain;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.security.AuthBarrier;
import org.cotrix.user.User;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ContextController;
import org.jglue.cdiunit.DummyHttpRequest;
import org.jglue.cdiunit.InSessionScope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(CdiRunner.class) 
public class BarrierTest {

	@Inject
	ContextController contextController;
	
	@Inject
	BeanSession session;
	
	@Mock
	FilterChain chain;
	
	@Inject
	AuthBarrier barrier;
	
	@Test @InSessionScope
	public void barrierDefaultsToGuest() throws Exception {

		contextController.openRequest(new DummyHttpRequest());
		
		barrier.doFilter(null, null, chain);
		
		assertSame(guest,session.get(User.class));
		
		verify(chain).doFilter(null,null);
		
		contextController.closeRequest();
		
	}
	
	@Test
	public void barrierChecksUser() throws Exception {

		contextController.openRequest(new DummyHttpRequest());
		
		session.add(User.class,cotrix);
		
		barrier.doFilter(null, null, chain);
		
		assertSame(cotrix,session.get(User.class));
		
		verify(chain).doFilter(null,null);
		
		contextController.closeRequest();
		
	}
}
