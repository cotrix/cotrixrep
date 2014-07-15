package org.cotrix.common.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DefaultReportingFuture<T> implements ReportingFuture<T> {
	
	private final Future<T> inner;
	private final Task context;
	private final Object cancelMonitor;
	
	public DefaultReportingFuture(Future<T> future, Task context,Object cancelMonitor) {
		this.inner=future;
		this.context=context;
		this.cancelMonitor=cancelMonitor;
	}
	
	@Override
	public T get() throws InterruptedException, ExecutionException {
		return inner.get();
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		
		synchronized (cancelMonitor) {
			
			try {
				
				TaskUpdate update = get(TaskUpdate.class);
				
				return update==null || update.progress()<1.0f ?
					inner.cancel(mayInterruptIfRunning) :
					false;
					
			}
			
			catch(ExecutionException e) {
				return false;
			}
			
		}
	}
	
	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return inner.get(timeout, unit);
	}
	
	@Override
	public boolean isDone() {
		return inner.isDone();
	}
	
	@Override
	public boolean isCancelled() {
		return inner.isCancelled();
	}
	
	@Override
	public <S> ForClause<T,S> register(final TaskObserver<S> observer) {
		return new ForClause<T,S>() {
			@Override
			public ReportingFuture<T> forType(Class<S> type) {
				context.add(type, observer);
				return DefaultReportingFuture.this;
			}
		};
	}
	
	@Override
	public <S> S get(Class<S> type) throws ExecutionException {
		return context.get(type);
	}
	
}
