package org.cotrix.common.tx;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionalFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(TransactionalFilter.class);
	
	@Inject
	Transactions txs;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		
		String path = HttpServletRequest.class.cast(request).getPathInfo();
		
		log.trace("start transaction for request @ {} ",path);
		
		try (
	
			Transaction tx = txs.open();
		) 
		{
			chain.doFilter(request, response);
			
			log.trace("committing  transaction for request @ {} ",path);
			
			tx.commit();
		}
		
	}

	@Override
	public void destroy() {
	}
}

