package org.acme;

import static junit.framework.Assert.*;
import static org.cotrix.user.PredefinedUsers.*;
import static org.mockito.Mockito.*;

import javax.inject.Inject;
import javax.servlet.FilterChain;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.security.AuthBarrier;
import org.cotrix.user.User;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.DummyHttpRequest;
import org.jglue.cdiunit.InSessionScope;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(CdiRunner.class) @InSessionScope
@AdditionalClasses(DummyHttpRequest.class)
public class BarrierTest {

	@Inject
	BeanSession session;
	
	@Mock
	FilterChain chain;
	
	@Inject
	AuthBarrier barrier;
	
	@Test
	public void barrierDefaultsToGuest() throws Exception {

		barrier.doFilter(null, null, chain);
		
		assertSame(guest,session.get(User.class));
		
		verify(chain).doFilter(null,null);
		
	}
	
	@Test
	public void barrierChecksUser() throws Exception {

		session.add(User.class,cotrix);
		
		barrier.doFilter(null, null, chain);
		
		assertSame(cotrix,session.get(User.class));
		
		verify(chain).doFilter(null,null);
		
	}
}
