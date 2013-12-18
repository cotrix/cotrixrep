package org.acme;

import static org.cotrix.domain.dsl.Users.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;

import org.cotrix.common.cdi.BeanSession;
import org.cotrix.common.cdi.Current;
import org.cotrix.domain.user.User;
import org.cotrix.security.AuthBarrier;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.googlecode.jeeunit.JeeunitRunner;

@RunWith(JeeunitRunner.class) 
public class BarrierTest {

	@Inject @Current
	BeanSession session;
	
	FilterChain chain = mock(FilterChain.class);
	
	@Inject
	AuthBarrier barrier;

	HttpServletRequest req = mock(HttpServletRequest.class);
	
	@Test
	//@TestControl(startScopes = SessionScoped.class)
	public void barrierDefaultsToGuest() throws Exception {

		session.clear(User.class);
		
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
