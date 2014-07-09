package org.cotrix.common.async;

import static org.cotrix.common.CommonUtils.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.cotrix.common.async.TaskEvents.EndTask;
import org.cotrix.common.async.TaskEvents.StartTask;
import org.cotrix.common.async.TaskEvents.StartTask.InfoProvider;
import org.cotrix.common.events.Current;

@ApplicationScoped
public class DefaultExecutionService implements ExecutionService {
	
	private static ExecutorService service = Executors.newCachedThreadPool();
	
	@Inject
	private TaskContext context;
	
	@Inject
	private Event<Object> event;
	
	@Inject @Current
	private InfoProvider info;
	
	private static class Closure {	
		Task t; 
	}
	
	
	@Override
	public <T> ReportingFuture<T> execute(final Callable<T> task) throws RejectedExecutionException {
	
		notNull("task", task);
		
		try {
			
		
			final CountDownLatch started = new CountDownLatch(1);
			
			final Closure closure = new Closure();
			
			final StartTask start = new StartTask(info.get());
			
			Callable<T> wrap = new Callable<T>() {
				

				@Override
				public T call() throws Exception {
				
					event.fire(start);
					
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
						
						event.fire(EndTask.instance);
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
