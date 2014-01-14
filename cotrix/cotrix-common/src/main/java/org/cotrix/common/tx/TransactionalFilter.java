package org.cotrix.common.tx;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class TransactionalFilter implements Filter {

	@Inject
	Transactions txs;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		
		try (
			Transaction tx = txs.open();
		) 
		{
			chain.doFilter(request, response);
			
			tx.commit();
		}
		
	}

	@Override
	public void destroy() {
	}
}

