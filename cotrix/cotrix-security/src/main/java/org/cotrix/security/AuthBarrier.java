package org.cotrix.security;

import static org.cotrix.domain.dsl.Users.*;

import java.io.IOException;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cotrix.common.BeanSession;
import org.cotrix.common.events.Current;
import org.cotrix.common.events.ApplicationLifecycleEvents.ApplicationEvent;
import org.cotrix.common.events.ApplicationLifecycleEvents.EndRequest;
import org.cotrix.common.events.ApplicationLifecycleEvents.StartRequest;
import org.cotrix.domain.user.User;

public class AuthBarrier implements Filter {

	@Inject @Current
	private BeanSession session;
	
	@Inject
	Event<ApplicationEvent> events;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		
		try {
			session.get(User.class);
		}
		catch(IllegalStateException e) {

			//defaults to guest for unauthenticated users
			session.add(User.class, guest);
			
		}
		
		events.fire(StartRequest.INSTANCE);
		
		chain.doFilter(request, response);
		
		events.fire(EndRequest.INSTANCE);
	}

	@Override
	public void destroy() {
	}
}
