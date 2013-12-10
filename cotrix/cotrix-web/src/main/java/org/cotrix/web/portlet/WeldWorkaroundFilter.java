/**
 * 
 */
package org.cotrix.web.portlet;

import java.io.IOException;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.common.cdi.ApplicationEvents.Startup;
import org.jboss.weld.servlet.SessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author "Federico De Faveri federico.defaveri@fao.org"
 *
 */
public class WeldWorkaroundFilter implements Filter {
	
	protected Logger logger = LoggerFactory.getLogger(WeldWorkaroundFilter.class);
	

	@Inject
	Event<ApplicationEvents.Startup> startup;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,	FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpSession session = httpServletRequest.getSession();
		SessionHolder.sessionCreated(session);
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.trace("initializing WeldWorkaroundFilter");

		startup.fire(Startup.INSTANCE);
	}

}
