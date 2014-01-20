/**
 * 
 */
package org.cotrix.common.tx;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Interceptor 
@Transactional  
@Priority(RUNTIME)
public class TransactionInterceptor {

	protected Logger log = LoggerFactory.getLogger(TransactionInterceptor.class);

	@Inject
	Transactions transactions;
	
	
	@AroundInvoke
	public Object executeAsTransaction(final InvocationContext ctx) throws Exception {
		
		if (
				ctx.getMethod().isAnnotationPresent(Transactional.class) 
				
				||
				
				ctx.getMethod().getDeclaringClass().isAnnotationPresent(Transactional.class)
				
			)
		
			try 
			
			(
			   Transaction tx = transactions.open();
			)
			
			{
				
				log.trace("in transaction for {}",ctx.getMethod());
				
				long time = System.currentTimeMillis();
				
				Object result = ctx.proceed();
				
				log.trace("transaction for {} complete in {} ms.",ctx.getMethod(),System.currentTimeMillis()-time);
				
				tx.commit();
				
				return result;
			}
		
		else
			return ctx.proceed();
		
		
		
	}

}
