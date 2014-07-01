package org.cotrix.common.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public interface ReportingFuture<T> extends Future<T> {

	<S> S get(Class<S> type) throws ExecutionException;
	
	<S> ForClause<T,S> register(TaskObserver<S> observer);
	
	interface ForClause<T,S> {
		
		ReportingFuture<T> forType(Class<S> type);
	}
}
