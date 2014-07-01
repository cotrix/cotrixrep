package org.cotrix.common.async;

import static org.cotrix.common.Utils.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DefaultExecutionService implements ExecutionService {

	private static ExecutorService service = Executors.newCachedThreadPool();
	
	@Inject
	TaskContext context;

	private static class Closure {	
		Task t; 
	}
	
	
	@Override
	public <T> ReportingFuture<T> execute(final Callable<T> task) throws RejectedExecutionException {
	
		notNull("task", task);
		
		try {
			
			final CountDownLatch started = new CountDownLatch(1);
			
			final Closure closure = new Closure();
			
			Callable<T> wrap = new Callable<T>() {
				
				@Override
				public T call() throws Exception {
				
					try {
						
						closure.t = context.thisTask();
						
						started.countDown();
						
						return task.call();
						
					}
					catch(Exception e) {
						context.thisTask().failed(e);
						throw e;
					}
					finally {
						context.reset();
					}
				}
			};
			
			Future<T> future = service.submit(wrap);
				
			started.await();
			
			return new DefaultReportingFuture<T>(future,closure.t);

		}
		catch(Exception e) {
			throw unchecked(e);
		}
	}

}
